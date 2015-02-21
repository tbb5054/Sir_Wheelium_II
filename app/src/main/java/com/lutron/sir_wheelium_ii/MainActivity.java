package com.lutron.sir_wheelium_ii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

// Note: This Main Activity relates to the launcher_activity_xml
public class MainActivity extends ActionBarActivity {

    // values to represent 1% and 1 degree value;
    private double MOTOR_PERCENT_FACTOR = 655.35;
    private double ANGLE_FACTOR = 182.0417;

    // byte value constants for the motors and servos
    private byte TOP_MOTOR_VALUE = 0x00;
    private byte BOTTOM_MOTOR_VALUE = 0X01;
    private byte X_SERVO_VALUE = 0X00;
    private byte Y_SERVO_VALUE = 0X01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.bluetooth_settings) {
            Intent intent = new Intent(this, BluetoothConnectionActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*******************
     *******************
        Actions
     *******************
     *******************/

    public void onLaunchButtonClicked(View view)
    {
        String topMotor = buildTopMotorCommand();
        String bottomMotor = buildBottomMotorCommand();
        String xAngle = buildXAngleCommand();
        String yAngle = buildYAngleCommand();
        String launchBalls = buildLaunchBallsCommand();

        String outPutString = String.format("TopMotor:%s\nBottomMotor:%s\nXAngle:%s\nYAngle%s\nLaunchBalls:%s",
                                            buildTopMotorCommand(),
                                            buildBottomMotorCommand(),
                                            buildXAngleCommand(),
                                            buildYAngleCommand(),
                                            buildLaunchBallsCommand());

        System.out.println(outPutString);
    }


    /*******************
     *******************
        Building Commands
     *******************
     *******************/

    public String buildTopMotorCommand()
    {
        byte commandByte = Command.AdjustSpeed.getNumVal();
        byte motorNumberByte = TOP_MOTOR_VALUE;

        int motorValue = (int)(getTopMotorLevel() * MOTOR_PERCENT_FACTOR);
        byte leastSignificantByte = (byte)motorValue;
        byte mostSignificantByte = (byte)(motorValue >>> 8);

        return String.format("0x%02X%02X%02X%02X%02X%02X",
                             commandByte,
                             motorNumberByte,
                             mostSignificantByte,
                             leastSignificantByte,
                             0x00,
                             0x00);
    }

    public String buildBottomMotorCommand()
    {
        byte commandByte = Command.AdjustSpeed.getNumVal();
        byte motorNumberByte = BOTTOM_MOTOR_VALUE;

        int motorValue = (int)(getBottomMotorLevel() * MOTOR_PERCENT_FACTOR);
        byte leastSignificantByte = (byte)motorValue;
        byte mostSignificantByte = (byte)(motorValue >>> 8);

        return String.format("0x%02X%02X%02X%02X%02X%02X",
                             commandByte,
                             motorNumberByte,
                             mostSignificantByte,
                             leastSignificantByte,
                             0x00,
                             0x00);
    }

    public String buildXAngleCommand()
    {
        byte commandByte = Command.AdjustAngle.getNumVal();
        byte servoNumberByte = X_SERVO_VALUE;

        int servoValue = (int)(getXAngle() * ANGLE_FACTOR);
        byte leastSignificantByte = (byte)servoValue;
        byte mostSignificantByte = (byte)(servoValue >>> 8);

        return String.format("0x%02X%02X%02X%02X%02X%02X",
                             commandByte,
                             servoNumberByte,
                             mostSignificantByte,
                             leastSignificantByte,
                             0x00,
                             0x00);
    }

    public String buildYAngleCommand()
    {
        byte commandByte = Command.AdjustAngle.getNumVal();
        byte servoNumberByte = Y_SERVO_VALUE;

        int servoValue = (int)(getYAngle() * ANGLE_FACTOR);
        byte leastSignificantByte = (byte)servoValue;
        byte mostSignificantByte = (byte)(servoValue >>> 8);

        return String.format("0x%02X%02X%02X%02X%02X%02X",
                             commandByte,
                             servoNumberByte,
                             mostSignificantByte,
                             leastSignificantByte,
                             0x00,
                             0x00);
    }

    public String buildLaunchBallsCommand()
    {
        byte commandByte = Command.LaunchBalls.getNumVal();

        int numberOfBalls = getNumberOfBalls();
        byte ballsByte = (byte)numberOfBalls;

        return String.format("0x%02X%02X%02X%02X%02X%02X",
                             commandByte,
                             ballsByte,
                             0x00,
                             0x00,
                             0x00,
                             0x00);
    }

    /*******************
     *******************
     Other Methods
     *******************
     *******************/
    public int getTopMotorLevel() {
        TextView topMotorTextView = (TextView)findViewById(R.id.topMotorSpeedEditText);

        return Integer.parseInt(topMotorTextView.getText().toString());
    }

    // Bottom Motor Protocol
    public int getBottomMotorLevel() {
        TextView bottomMotorTextView = (TextView)findViewById(R.id.bottomMotorSpeedTextView);

        return Integer.parseInt(bottomMotorTextView.getText().toString());
    }

    // X Angle Protocol
    public int getXAngle() {
        TextView xAngleTextView = (TextView)findViewById(R.id.xAngleTextView);

        return Integer.parseInt(xAngleTextView.getText().toString());
    }

    // Y Angle Protocol
    public int getYAngle() {
        TextView yAngleTextView = (TextView)findViewById(R.id.yAngleTextView);

        return Integer.parseInt(yAngleTextView.getText().toString());
    }

    public int getNumberOfBalls(){
        // TODO add support to launch multiple balls
        return 1;
    }
}
