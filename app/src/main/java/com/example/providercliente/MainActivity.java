package com.example.providercliente;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    Button btnAdd, btnSearch;
    EditText txtFilter;
    SimpleCursorAdapter adp;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnAdd = findViewById(R.id.btnAgregarC);
        btnSearch = findViewById(R.id.btnBuscar);
        txtFilter = findViewById(R.id.txtBuscar);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String identificarFiltro = txtFilter.getText().toString();
                String[] obtenerTexto = {txtFilter.getText().toString()};
                if (identificarFiltro.matches("")) {
                    /*Cursor c = getContentResolver().query(
                            ContractCPContactos.CONTENT_URI,
                            ContractCPContactos.PROJECTION,
                            null,
                            null,
                            null
                    );*/
                    onActivityResult(1000, -1, getIntent());
                    //refrescarLista(c);
                } else {
                    /*Cursor c = getContentResolver().query(
                            ContractCPContactos.CONTENT_URI,
                            ContractCPContactos.PROJECTION,
                            "usuario LIKE ?",
                            obtenerTexto,
                            null
                    );*/
                    onActivityResult(1000, -1, getIntent());
                    //refrescarLista(c);
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registro.class);
                startActivityForResult(intent, 1000);
            }
        });
        onActivityResult(1000, -1, getIntent());


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                final Cursor cursor = (Cursor) adp.getItem(position);
                String obtenerId = String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")));
                final String where = ContentProviderContactos.FIELD_ID + " = ?";
                final String[] argumentos = {obtenerId};

                AlertDialog.Builder menu = new AlertDialog.Builder(MainActivity.this);
                CharSequence[] opciones = {"Editar", "Eliminar"};
                menu.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int opcion) {
                        switch (opcion) {
                            case 0://editar
                                Intent intent = new Intent(MainActivity.this, Editar.class);
                                intent.putExtra("_id", cursor.getInt(cursor.getColumnIndex("_id")));
                                intent.putExtra("usuario", cursor.getString(cursor.getColumnIndex("usuario")));
                                intent.putExtra("email", cursor.getString(cursor.getColumnIndex("email")));
                                intent.putExtra("tel", cursor.getString(cursor.getColumnIndex("tel")));
                                intent.putExtra("fechaNac", cursor.getString(cursor.getColumnIndex("fechaNac")));
                                //startActivity(intent);
                                startActivityForResult(intent, 1000);
                                break;
                            case 1://eliminar
                                Snackbar.make(view, "¿Estás seguro", Snackbar.LENGTH_LONG).setAction("SI",
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                /*String contact = contactoSeleccionado.id+"";
                                                dao.delete(contact);
                                                onActivityResult(1000,-1,getIntent());
                                                Toast.makeText(MainActivity.this, "elemento seleccionado: "+contact, Toast.LENGTH_SHORT).show();
                                                 */
                                                getContentResolver().delete(ContentProviderContactos.CONTENT_URI, where, argumentos);
                                                onActivityResult(1000, -1, getIntent());
                                            }
                                        }).show();
                                break;
                        }
                    }
                });
                menu.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            /*DAOContacto dao = new DAOContacto(this);
            c = dao.getAllCursor();

            lv = findViewById(R.id.lv);*/
            c = getContentResolver().query(Uri.withAppendedPath(ContentProviderContactos.CONTENT_URI, ""),
                    ContentProviderContactos.PROJECTION, null, null, null);
            lv = findViewById(R.id.lv);
            adp = new SimpleCursorAdapter
                    (this, android.R.layout.simple_list_item_2,
                            c,
                            new String[]{
                                    ContentProviderContactos.FIELD_USUARIO,
                                    ContentProviderContactos.FIELD_EMAIL},
                            new int[]{android.R.id.text1,
                                    android.R.id.text2},
                            SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE);
            lv.setAdapter(adp);
        }
    }
}