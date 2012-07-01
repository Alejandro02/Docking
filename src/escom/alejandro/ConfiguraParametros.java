package escom.alejandro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import escom.alejandro.R.id;

public class ConfiguraParametros extends Activity implements OnClickListener {
	View botonIniciar;
	TextView texto;
	EditText editText;
	CheckBox chk;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        botonIniciar = findViewById(id.botonEmpezar);
        botonIniciar.setOnClickListener(this);
    }

	public void onClick(View v) {
		Intent i = new Intent(this,Lienzo.class);
		
		editText = (EditText) findViewById(id.poblacion);
		i.putExtra("param1", Integer.parseInt(editText.getText().toString()));
		editText = (EditText) findViewById(id.mutacion);
		i.putExtra("param2", Float.parseFloat(editText.getText().toString()));
		editText = (EditText) findViewById(id.individuosElite);
		i.putExtra("param3", Integer.parseInt(editText.getText().toString()));
		editText = (EditText) findViewById(id.coeficienteAtraccion);
		i.putExtra("param4", Integer.parseInt(editText.getText().toString()));
		editText = (EditText) findViewById(id.umbralGemelos);
		i.putExtra("param5", Integer.parseInt(editText.getText().toString()));
		chk = (CheckBox) findViewById(id.eliminarGemelos);
		i.putExtra("param6", chk.isChecked());
		
		startActivity(i);
	}
}

