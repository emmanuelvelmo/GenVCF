package com.example.genvcf;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity
{
    private static final int CREATE_FILE_REQUEST_CODE = 123;
    private EditText TextBox1_2;
    private EditText TextBox2_2;
    private EditText TextBox4_2;
    private EditText TextBox3_2;
    private StringBuilder content;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);

        TextBox1_2 = findViewById(R.id.TextBox1);
        TextBox2_2 = findViewById(R.id.TextBox2);
        TextBox3_2 = findViewById(R.id.TextBox3);
        TextBox4_2 = findViewById(R.id.TextBox4);

        content = new StringBuilder();
    }

    public void onGenerateButtonClick(View view)
    {
        String TextBox1_s = TextBox1_2.getText().toString();
        String TextBox2_s = TextBox2_2.getText().toString();
        String TextBox3_s = TextBox3_2.getText().toString();
        String TextBox4_s = TextBox4_2.getText().toString();

        int var_quantity = Integer.parseInt(TextBox4_s);
        int area_dig = 7;
        try
        {
            if (!TextBox3_s.isEmpty())
            {
                area_dig = Integer.parseInt(TextBox3_s);
            }
        }
        catch (NumberFormatException e)
        {
            area_dig = 7;
        }

        content.setLength(0);

        for (int i = 0; i < var_quantity; i++)
        {
            StringBuilder bufferTextBox2_s = new StringBuilder();

            for(int j = 0; j < area_dig; j++)
            {
                bufferTextBox2_s.append(new java.util.Random().nextInt(10));
            }

            content.append("BEGIN:VCARD\n");
            content.append("VERSION:2.1\n");
            content.append("N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:").append((i + 1)).append(";;\n");
            content.append("FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:").append((i + 1)).append("\n");
            content.append("TEL;CELL:+").append(TextBox1_s).append(TextBox2_s).append(bufferTextBox2_s).append("\n");
            content.append("END:VCARD\n");
        }

        Intent intent1 = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent1.addCategory(Intent.CATEGORY_OPENABLE);
        intent1.setType("text/vcard");
        intent1.putExtra(Intent.EXTRA_TITLE, "Generated VCF.vcf");

        startActivityForResult(intent1, CREATE_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();

            try
            {
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

                outputStreamWriter.write(content.toString());

                outputStreamWriter.close();
                outputStream.close();

                showToast("VCF generated successfully: " + uri.toString());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                showToast("Error");
            }
        }
    }

    private void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
