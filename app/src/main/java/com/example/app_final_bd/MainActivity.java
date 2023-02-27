package com.example.app_final_bd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton guardar, verDatos, modificar, borrar;
    EditText nombre, apellido;

    ListView lista;
    private static final String NOMBRE_BASE_DATOS = "users.db";
    private static final int VERSION_ACTUAL = 1;
    MiBaseDeDatos miDB;
    SQLiteDatabase db;

    boolean selecionado = false;

    Datos datos1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = findViewById(R.id.editTextTextPersonName);
        apellido = findViewById(R.id.editTextTextPersonName2);
        guardar = findViewById(R.id.imageButtonGuardar);
        verDatos = findViewById(R.id.imageButtonVerDatos);
        modificar = findViewById(R.id.imageButtonModificar);
        borrar = findViewById(R.id.imageButtonBorrar);
        guardar.setOnClickListener(this);
        verDatos.setOnClickListener(this);
        modificar.setOnClickListener(this);
        borrar.setOnClickListener(this);

        miDB = new MiBaseDeDatos(getApplicationContext(), NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        db = miDB.getWritableDatabase();
        lista = findViewById(R.id.lista);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButtonGuardar:
                String name = nombre.getText().toString();
                String surname = apellido.getText().toString();
                db.execSQL("INSERT INTO User (name,surname) VALUES ('" + name + "','" + surname + "')");
                Toast.makeText(this, "Se ha insertado un registro en la base de datos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButtonVerDatos:
                Datos[] datos;
                Cursor miCursor = db.rawQuery("SELECT rowid, name, surname FROM User", null);
                if (miCursor.moveToFirst()) {
                    datos = new Datos[miCursor.getCount()];
                    int index = 0;
                    do {
                        String codigo = miCursor.getString(0);
                        String nombre = miCursor.getString(1);
                        String surname2 = miCursor.getString(2);
                        datos[index] = new Datos(codigo, nombre, surname2);
                        index++;
                    } while (miCursor.moveToNext());
                } else {
                    datos = new Datos[0];
                }

                Adaptador miAdaptador = new Adaptador(this, datos);
                lista.setAdapter(miAdaptador);

                if (lista.getHeaderViewsCount() == 0) {
                    View cabecera = getLayoutInflater().inflate(R.layout.cabecera, null);
                    lista.addHeaderView(cabecera);
                }

                break;
            case R.id.imageButtonModificar:
                if(!selecionado){
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Obtener el registro seleccionado
                             datos1 = (Datos) parent.getAdapter().getItem(position);

                            // Pre-populate the EditText fields with the current data
                            nombre.setText(datos1.getTexto2());
                            apellido.setText(datos1.getTexto3());
                            selecionado = true;
                        }
                    });

                }else if (selecionado){
                    // Mostrar el diálogo de confirmación
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Modificar registro");
                    builder.setMessage("¿Está seguro de que desea modificar el"+datos1.getTexto1()+" registro?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Obtener los nuevos datos de los EditText
                            String name = nombre.getText().toString();
                            String surname = apellido.getText().toString();

                            // Actualizar el registro en la base de datos
                            db.execSQL("UPDATE User SET name = '" + name + "', surname = '" + surname + "' WHERE rowid = " + datos1.getTexto1());

                            // Mostrar mensaje de confirmación
                            Toast.makeText(getApplicationContext(), "Registro modificado correctamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                    nombre.setText("");
                    apellido.setText("");
                }

                break;
            case R.id.imageButtonBorrar:
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Obtener el registro seleccionado

                        Datos datos = (Datos) parent.getAdapter().getItem(position);
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                        builder2.setTitle("Borrar registro");
                        builder2.setMessage("¿Está seguro de que desea borrar el "+datos.getTexto1()+" registro?");
                        builder2.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Borrar el registro de la base de datos
                                db.execSQL("DELETE FROM User WHERE rowid = " + datos.getTexto1());
                                // Mostrar mensaje de confirmación
                                Toast.makeText(getApplicationContext(), "Registro borrado correctamente", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder2.setNegativeButton("No", null);
                        builder2.show();
                    }
                });

                break;
        }
    }
}