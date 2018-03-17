package sport.fractionne;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.text.Editable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.text.TextWatcher;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String DEMANDE = "sport.fractionne.mademande";

    Button go = null;
    Button raz = null;

    EditText temps = null;
    EditText acceleration = null;
    EditText repos = null;
    RadioGroup group = null;
    RadioGroup group_mode=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        go = (Button)findViewById(R.id.go);

        raz = (Button)findViewById(R.id.raz);

        temps=(EditText)findViewById(R.id.duree);
        acceleration = (EditText)findViewById(R.id.acceleration);
        repos = (EditText)findViewById(R.id.repos);

        group = (RadioGroup)findViewById(R.id.group);
        group_mode = (RadioGroup)findViewById(R.id.group_mode);
        // On attribue un listener adapté aux vues qui en ont besoin
        go.setOnClickListener(envoyerListener);
        raz.setOnClickListener(razListener);
        acceleration.addTextChangedListener(textWatcher);
        repos.addTextChangedListener(textWatcher);
        temps.addTextChangedListener(textWatcher);

    }


    private View.OnClickListener envoyerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //boolean button1_checked = radioButton1.isChecked();
            String tem=temps.getText().toString();
            String accel=acceleration.getText().toString();
            String rep=repos.getText().toString();

            String[] demande1= new String[4];
            demande1[0]=accel;
            demande1[1]=rep;
            demande1[2]=tem;

            if(group_mode.getCheckedRadioButtonId()== R.id.mode1){
                demande1[3]="vibreur";
            }else{
                demande1[3]="bip";
            }
            if(group.getCheckedRadioButtonId() == R.id.radio1) {
                demande1[0] = String.valueOf(15);
                demande1[1] = String.valueOf(15);
            }
            if(group.getCheckedRadioButtonId() == R.id.radio2) {
                demande1[0] = String.valueOf(30);
                demande1[1] = String.valueOf(30);
            }
            if(group.getCheckedRadioButtonId() == R.id.radio3) {
                demande1[0] = String.valueOf(15);
                demande1[1] = String.valueOf(30);
            }
            if(group.getCheckedRadioButtonId() == R.id.radio4) {
                demande1[0] = String.valueOf(30);
                demande1[1] = String.valueOf(15);
            }
            Intent chrono = new Intent(MainActivity.this, chrono.class);
            chrono.putExtra(DEMANDE,demande1);
            if(demande1[0].equalsIgnoreCase("") || demande1[1].equalsIgnoreCase("") ||
                    demande1[2].equalsIgnoreCase("")|| demande1[3].equalsIgnoreCase("")){
                Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }else {
                startActivity(chrono);
            }
        }
    };

    // Listener du bouton de remise à zéro
    private View.OnClickListener razListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            temps.getText().clear();
            acceleration.getText().clear();
            repos.getText().clear();
            group.clearCheck();
            group_mode.clearCheck();
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
