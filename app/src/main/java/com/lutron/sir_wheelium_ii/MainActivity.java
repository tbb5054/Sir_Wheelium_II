package com.lutron.sir_wheelium_ii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.lutron.sir_wheelium_ii.BluetoothConnectionActivity.ConnectedThread;

// Note: This Main Activity relates to the launcher_activity_xml
public class MainActivity extends ActionBarActivity {

    private int BLUE_TOOTH_REQUEST_CODE = 555;
    // values to represent 1% and 1 degree value;
    private double MOTOR_PERCENT_FACTOR = 655.35;
    private double ANGLE_FACTOR = 182.0417;

    // byte value constants for the motors and servos
    private byte TOP_MOTOR_VALUE = 0x00;
    private byte BOTTOM_MOTOR_VALUE = 0X01;
    private byte X_SERVO_VALUE = 0X02;
    private byte Y_SERVO_VALUE = 0X03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);

        MyApplication myApplication = ((MyApplication)getApplicationContext());

        Button counterClockButton = (Button)findViewById(R.id.xCounterClockwiseButton);
        OnTouchListener counterClockListener=new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MyApplication myApplication = ((MyApplication)getApplicationContext());
                    ConnectedThread connectedThread = myApplication.getConnectedThread();

                    // set top motor
                    byte[] launchBytes = buildStartCounterClockWiseCommand();
                    connectedThread.write(launchBytes);
                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                    MyApplication myApplication = ((MyApplication)getApplicationContext());
                    ConnectedThread connectedThread = myApplication.getConnectedThread();

                    // set top motor
                    byte[] launchBytes = buildStopXRotationCommand();
                    connectedThread.write(launchBytes);
                }
                return false;
            }
        };
        counterClockButton.setOnTouchListener(counterClockListener);

        Button clockButton = (Button)findViewById(R.id.xClockwiseButton);
        OnTouchListener clockListener=new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MyApplication myApplication = ((MyApplication)getApplicationContext());
                    ConnectedThread connectedThread = myApplication.getConnectedThread();

                    // set top motor
                    byte[] launchBytes = buildStartClockWiseCommand();
                    connectedThread.write(launchBytes);
                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                    MyApplication myApplication = ((MyApplication)getApplicationContext());
                    ConnectedThread connectedThread = myApplication.getConnectedThread();

                    // set top motor
                    byte[] launchBytes = buildStopXRotationCommand();
                    connectedThread.write(launchBytes);
                }
                return false;
            }
        };
        clockButton.setOnTouchListener(clockListener);
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
            startActivityForResult(intent, BLUE_TOOTH_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*******************
     *******************
        Actions
     *******************
     *******************/

    public void onXCounterClicked(View view){
        //String counterStartString = buildXAngleCommand(Rotation.CounterClockWise);
    }

    public void onXClockClicked(View view)    {
        //String counterClockStartString = buildXAngleCommand(Rotation.CounterClockWise);
    }

    public void onUpdateMotorClick(View view){
        MyApplication myApplication = ((MyApplication)getApplicationContext());
        ConnectedThread connectedThread = myApplication.getConnectedThread();

        // set top motor
        byte[] launchBytes = buildTopMotorCommand();
        connectedThread.write(launchBytes);

        // set bottom motor
        launchBytes = buildBottomMotorCommand();
        connectedThread.write(launchBytes);
    }

    public void onKillMotorClick(View view){
        MyApplication myApplication = ((MyApplication)getApplicationContext());
        ConnectedThread connectedThread = myApplication.getConnectedThread();
        byte[] launchBytes = buildKillMotorCommand();
        connectedThread.write(launchBytes);
    }

    public void onYClick(View view){
        MyApplication myApplication = ((MyApplication)getApplicationContext());
        ConnectedThread connectedThread = myApplication.getConnectedThread();
        byte[] launchBytes = buildYAngleCommand();
        connectedThread.write(launchBytes);
    }

    public void onLaunchButtonClicked(View view)
    {
        MyApplication myApplication = ((MyApplication)getApplicationContext());
        ConnectedThread connectedThread = myApplication.getConnectedThread();
        byte[] launchBytes = buildLaunchBallsCommand();
        connectedThread.write(launchBytes);
    }


    /*******************
     *******************
        Building Commands
     *******************
     *******************/

    public byte[] buildKillMotorCommand(){
        return new byte[]{
            Command.KillMotors.getNumVal(),
            0x00,
            0x00,
            0x00,
            0x00,
            0x00
        };
    }

    public byte[] buildTopMotorCommand()
    {
        byte commandByte = Command.AdjustSpeed.getNumVal();
        byte motorNumberByte = TOP_MOTOR_VALUE;

        int motorValue = (int)(getTopMotorLevel() * MOTOR_PERCENT_FACTOR);
        byte leastSignificantByte = (byte)motorValue;
        byte mostSignificantByte = (byte)(motorValue >>> 8);

        return new byte[]{
                commandByte,
                motorNumberByte,
                mostSignificantByte,
                leastSignificantByte,
                0x00,
                0x00
        };
    }

    public byte[] buildBottomMotorCommand()
    {
        byte commandByte = Command.AdjustSpeed.getNumVal();
        byte motorNumberByte = BOTTOM_MOTOR_VALUE;

        int motorValue = (int)(getBottomMotorLevel() * MOTOR_PERCENT_FACTOR);
        byte leastSignificantByte = (byte)motorValue;
        byte mostSignificantByte = (byte)(motorValue >>> 8);

        return new byte[]{
                commandByte,
                motorNumberByte,
                mostSignificantByte,
                leastSignificantByte,
                0x00,
                0x00
        };
    }

    public byte[] buildStartClockWiseCommand(){
        byte commandByte = Command.AdjustAngle.getNumVal();
        byte servoNumberByte = X_SERVO_VALUE;

        return new byte[]{
                commandByte,
                servoNumberByte,
                (byte)0x8f,
                (byte)0xff,
                0x00,
                0x00
        };
    }

    public byte[] buildStartCounterClockWiseCommand(){
        byte commandByte = Command.AdjustAngle.getNumVal();
        byte servoNumberByte = X_SERVO_VALUE;

        return new byte[]{
                commandByte,
                servoNumberByte,
                (byte)0x6f,
                (byte)0xff,
                0x00,
                0x00
        };
    }

    public byte[] buildStopXRotationCommand(){
        byte commandByte = Command.AdjustAngle.getNumVal();
        byte servoNumberByte = X_SERVO_VALUE;

        return new byte[]{
                commandByte,
                servoNumberByte,
                (byte)0x7f,
                (byte)0xff,
                0x00,
                0x00
        };
    }

    public byte[] buildYAngleCommand()
    {
        byte commandByte = Command.AdjustAngle.getNumVal();
        byte servoNumberByte = Y_SERVO_VALUE;

        int servoValue = (int)(getYAngle() * ANGLE_FACTOR);
        byte leastSignificantByte = (byte)servoValue;
        byte mostSignificantByte = (byte)(servoValue >>> 8);

        if(servoValue/ANGLE_FACTOR < 90 || servoValue/ANGLE_FACTOR >150 )
        {
            servoValue = (int)(90 * ANGLE_FACTOR);
            leastSignificantByte = (byte)servoValue;
            mostSignificantByte = (byte)(servoValue >>> 8);
        }

        return new byte[]{
                commandByte,
                servoNumberByte,
                mostSignificantByte,
                leastSignificantByte,
                0x00,
                0x00
        };
    }

    public byte[] buildLaunchBallsCommand()
    {
        byte commandByte = Command.LaunchBalls.getNumVal();

        int numberOfBalls = getNumberOfBalls();
        byte ballsByte = (byte)numberOfBalls;

        return new byte[]{
                commandByte,
                ballsByte,
                0x00,
                0x00,
                0x00,
                0x00
        };
    }

    /*******************
     *******************
     Other Methods
     *******************
     *******************/
    public int getTopMotorLevel() {
        TextView topMotorTextView = (TextView)findViewById(R.id.topMotorSpeedEditText);

        if(topMotorTextView.getText().toString().matches("")){
            topMotorTextView.setText("0");
        }
        return Integer.parseInt(topMotorTextView.getText().toString());
    }

    // Bottom Motor Protocol
    public int getBottomMotorLevel() {
        TextView bottomMotorTextView = (TextView)findViewById(R.id.bottomMotorSpeedTextView);

        if(bottomMotorTextView.getText().toString().matches("")){
            bottomMotorTextView.setText("0");
        }
        return Integer.parseInt(bottomMotorTextView.getText().toString());
    }

    // Y Angle Protocol
    public int getYAngle() {
        TextView yAngleTextView = (TextView)findViewById(R.id.yAngleTextView);

        if(yAngleTextView.getText().toString().matches("")){
            yAngleTextView.setText("90");
        }
        return Integer.parseInt(yAngleTextView.getText().toString());
    }

    public int getNumberOfBalls(){
        TextView ballsEditText = (TextView)findViewById(R.id.ballsEditText);

        if(ballsEditText.getText().toString().matches("")){
            ballsEditText.setText("0");
        }

        return Integer.parseInt(ballsEditText.getText().toString());
    }
}
