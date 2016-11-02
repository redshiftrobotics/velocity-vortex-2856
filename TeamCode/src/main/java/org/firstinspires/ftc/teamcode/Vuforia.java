package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.Range;
import com.vuforia.HINT;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Duncan on 10/25/2016.
 */

@Autonomous(name = "Vuforia")
public class Vuforia extends LinearOpMode {
    Robot robot;
    DcMotor m0;
    DcMotor m1;
    DcMotor m2;
    DcMotor m3;

    VuforiaLocalizer vuforiaLocalizer;
    VuforiaLocalizer.Parameters parameters;
    VuforiaTrackables visionTargets;
    VuforiaTrackable target;
    VuforiaTrackableDefaultListener listener;

    OpenGLMatrix lastKnownLocation;
    OpenGLMatrix phoneLocation;

    public static final String VUFORIA_KEY = "AZSn6x3/////AAAAGQUiAVV7BUM5p1/oUpgt2zd2gpH6mH3RDbbzWwc6oPE80fZ61JSft68k7bnar35QeFYAffqqC4lASNO+ufDo3YkAAmrqm7xttuFSQCwStUUwxj6smqRehkzjIG9Ud/qMUKwtZ477dal9IayK0S/meM6t8xQpLOfGpFesBjXBxqaO092Uz3ab+O+Y3px+tSwo+w7NTqDKy6QhJnju6vyqLN10tXhzAYCdsl0tPmNoYfieelsQNAfQrTO0onkzGrvJXsSF+J+eVbwVUtdn1+SK2MWyVQHks/aXvin929RYaMTgxiAz6GwmKOHR5/S4XarDBz48mKGSnxB00OOg8QxFSWkKPsHen5b9ZQpVFwcqdzz0";

    private float robotX = 0;
    private float robotY = 0;
    private float robotAngle = 0;

    public void runOpMode() throws InterruptedException
    {
        setupVuforia();

        m0 = hardwareMap.dcMotor.get("m0");
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");


        //robot = new Robot(m0, m1, m2, m3);

        lastKnownLocation = createMatrix(0, 0, 0, 0, 0, 0);

        waitForStart();

        visionTargets.activate();

        while(opModeIsActive())
        {
            OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();

            if(latestLocation != null)
                lastKnownLocation = latestLocation;

            float[] coordinates = lastKnownLocation.getTranslation().getData();

            robotX = coordinates[0];
            robotY = coordinates[1];
            robotAngle = Orientation.getOrientation(lastKnownLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;


            if(robotY>=50||robotY<=-50){
                moveHor(robotY/100);
            }else if(robotX>=50||robotX<=-50){
                moveVert(robotX/100);
            }/*else if(robotAngle>=95||robotAngle<=85){
                rotate((float) Range.clip((90-robotAngle)/90,-1.0,1.0));
            }*/else{
                //fullStop();
            }



            telemetry.addData("Tracking " + target.getName(), listener.isVisible());
            telemetry.addData("Last Known Location", formatMatix(lastKnownLocation));
            telemetry.addData("Robot X: ", robotX);
            telemetry.addData("Robot Y: ", robotY);
            telemetry.addData("Robot Angle: ", robotAngle);
            telemetry.update();
            idle();
        }
    }

    public void setupVuforia()
    {
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.useExtendedTracking = true;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");
        //Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_OBJECT_TARGETS, 4);


        target = visionTargets.get(0);
        target.setName("Wheels Target");
        target.setLocation(createMatrix(0, 0, 0, 90, 0, 90));

        phoneLocation = createMatrix(0, 0, 0, 0, 0, 0);

        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);
    }



    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w)
    {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    public String formatMatix(OpenGLMatrix matrix)
    {
        return matrix.formatAsTransform();
    }

    public void moveVert(float speed){
        if(speed > 0.05) speed = 0.3f;
        else if(speed < -0.05) speed = -0.3f;
        else speed = 0;
        m0.setPower(speed);
        m1.setPower(speed);
        m2.setPower(-speed);
        m3.setPower(-speed);
    }
    public void moveHor(float speed){
        if(speed > 0.05) speed = 0.3f;
        else if(speed < -0.05) speed = -0.3f;
        else speed = 0;
        m0.setPower(-speed);
        m1.setPower(-speed);
        m2.setPower(-speed);
        m3.setPower(-speed);
    }
    public void rotate(float speed){
        m0.setPower(speed);
        m1.setPower(speed);
        m2.setPower(speed);
        m3.setPower(speed);
    }
    public void fullStop(){
        m0.setPower(0);
        m1.setPower(0);
        m2.setPower(0);
        m3.setPower(0);
    }
}
