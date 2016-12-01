package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.*;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
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
 * Created by Duncan on 11/2/2016.
 */

public class Tracking {

    static final String VUFORIA_KEY = "AZSn6x3/////AAAAGQUiAVV7BUM5p1/oUpgt2zd2gpH6mH3RDbbzWwc6oPE80fZ61JSft68k7bnar35QeFYAffqqC4lASNO+ufDo3YkAAmrqm7xttuFSQCwStUUwxj6smqRehkzjIG9Ud/qMUKwtZ477dal9IayK0S/meM6t8xQpLOfGpFesBjXBxqaO092Uz3ab+O+Y3px+tSwo+w7NTqDKy6QhJnju6vyqLN10tXhzAYCdsl0tPmNoYfieelsQNAfQrTO0onkzGrvJXsSF+J+eVbwVUtdn1+SK2MWyVQHks/aXvin929RYaMTgxiAz6GwmKOHR5/S4XarDBz48mKGSnxB00OOg8QxFSWkKPsHen5b9ZQpVFwcqdzz0";
    static boolean alligned;
    static LocationObject location = new LocationObject(0, 0, 0);

    static VuforiaLocalizer.Parameters parameters;
    static VuforiaLocalizer vuforiaLocalizer;
    static VuforiaTrackables visionTargets;
    static VuforiaTrackable target;
    static VuforiaTrackableDefaultListener listener;

    static OpenGLMatrix lastKnownLocation;
    static OpenGLMatrix phoneLocation;

    public enum ImageType {
        Wheels, Tools, Legos, Gears
    }


    public static void Setup(ImageType image, VuforiaLocalizer.CameraDirection direction){
        parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = direction; //sets the camera used by vuforia
        parameters.useExtendedTracking = true; //sets the vuforia camera preview to display on the screen
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");
        //sets up the target to the correct image based off the input
        switch (image) {
            case Wheels:
                target = visionTargets.get(0);
                target.setName("Wheels Target");
                target.setLocation(createMatrix(0, 0, 0, 90, 0, 90));
                break;
            case Tools:
                target = visionTargets.get(1);
                target.setName("Tools Target");
                target.setLocation(createMatrix(0, 0, 0, 90, 0, 90));
                break;
            case Legos:
                target = visionTargets.get(2);
                target.setName("Legos Target");
                target.setLocation(createMatrix(0, 0, 0, 90, 0, 90));
                break;
            case Gears:
                target = visionTargets.get(3);
                target.setName("Gears Target");
                target.setLocation(createMatrix(0, 0, 0, 90, 0, 90));
                break;
            default:
                return;
        }

        phoneLocation = createMatrix(0, 0, 0, 0, 0, 0);

        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);



        lastKnownLocation = createMatrix(0, 0, 0, 0, 0, 0);
        visionTargets.activate();
    }

    public static void Align(Robot robot, Telemetry telemetry, float offSet) {

        while(!alligned){
            OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();

            if(latestLocation != null)
                lastKnownLocation = latestLocation;

            float[] coordinates = lastKnownLocation.getTranslation().getData();

            location.robotX = coordinates[0];
            location.robotY = coordinates[1];
            location.robotAngle = Orientation.getOrientation(lastKnownLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;

            //move(location, m0, m1, m2, m3);

            //moveHor(Range.clip(location.robotY/100,-1,1), m0, m1, m2, m3);
            //moveVert(Range.clip(location.robotX/100,-1,1), m0, m1, m2, m3);

            if(location.robotY>=20+offSet||location.robotY<=(-20)+offSet){
                robot.Straight(location.robotY/1000, new Float[]{1f,0f}, 6, telemetry);
//                moveVert(Range.clip(location.robotY/100,-1,1), m0, m1, m2, m3);
            }else if(location.robotX>=50||location.robotX<=-50){
                robot.moveInBlue(1);
//                moveHor(Range.clip(location.robotX/100,-1,1), m0, m1, m2, m3);
            }/*else if(robotAngle>=95||robotAngle<=85){
                rotate((float) Range.clip((90-robotAngle)/90,-1.0,1.0));
            }*/else{
                alligned = true;
            }
        }
        return;
    }


    public static void Center(Robot robot, Telemetry telemetry, float offSet) {

        while(!alligned){
            OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();

            if(latestLocation != null)
                lastKnownLocation = latestLocation;

            float[] coordinates = lastKnownLocation.getTranslation().getData();

            location.robotX = coordinates[0];
            location.robotY = coordinates[1];
            location.robotAngle = Orientation.getOrientation(lastKnownLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;

            //move(location, m0, m1, m2, m3);

            //moveHor(Range.clip(location.robotY/100,-1,1), m0, m1, m2, m3);
            //moveVert(Range.clip(location.robotX/100,-1,1), m0, m1, m2, m3);

            if(location.robotY>=20+offSet||location.robotY<=(-20)+offSet){
                robot.Straight(location.robotY/1000, new Float[]{1f,0f}, 6, telemetry);
//                moveVert(Range.clip(location.robotY/100,-1,1), m0, m1, m2, m3);
//            }else if(location.robotX>=50||location.robotX<=-50){

//                moveHor(Range.clip(location.robotX/100,-1,1), m0, m1, m2, m3);
            }/*else if(robotAngle>=95||robotAngle<=85){
                rotate((float) Range.clip((90-robotAngle)/90,-1.0,1.0));
            }*/else{
                alligned = true;
            }
        }
        return;
    }

    public static OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w)
    {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }
    public static String formatMatix(OpenGLMatrix matrix)
    {
        return matrix.formatAsTransform();
    }

//    public static void move(LocationObject locObject, DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3){
//        m0.setPower((-Range.clip(locObject.robotY/100,-1.0,1.0)+Range.clip(locObject.robotX/100,-1.0,1.0))/4);
//        m1.setPower((-Range.clip(locObject.robotY/100,-1.0,1.0)+Range.clip(locObject.robotX/100,-1.0,1.0))/4);
//        m2.setPower((Range.clip(locObject.robotY/100,-1.0,1.0)+Range.clip(locObject.robotX/100,-1.0,1.0))/4);
//        m3.setPower((Range.clip(locObject.robotY/100,-1.0,1.0)+Range.clip(locObject.robotX/100,-1.0,1.0))/4);
//    }
//
//    public static void moveVert(float speed, DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3){
//        m0.setPower(speed);
//        m1.setPower(speed);
//        m2.setPower(-speed);
//        m3.setPower(-speed);
//    }
//    public static void moveHor(float speed, DcMotor m0, DcMotor m1, DcMotor m2, DcMotor m3){
//        m0.setPower(-speed);
//        m1.setPower(-speed);
//        m2.setPower(-speed);
//        m3.setPower(-speed);
//    }
}