package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;


/**
 * Teleop2856 is the teleop op mode of 2856,
 * in the FTC game Velocity Vortex. Teleop2856
 * controls all motors on the robot, by the two
 * controllers. The first controller controls:
 * <ul>
 *     <li>The drive motors of the robot</li>
 *     <li>The shooter and collector of the particles</li>
 * </ul>
 * The second controller controls:
 * <ul>
 *     <li>The shooter and collector of the particles</li>
 *     <li>The capball mechanism (Not installed yet)</li>
 * </ul>
 * @author Duncan McKee
 * @version 1.1, 12/18/2016
 */
@TeleOp(name="2856 Teleop")
public class Teleop2856 extends OpMode {
    DcMotor motors[] = new DcMotor[4];
    DcMotor shooter;
    DcMotor collector;
    //DcMotor capballLift;
    int rotations;
    int collecting;
    boolean collectSwitch;
    boolean reseting;
    HardwareController hardwareController;

    @Override
    public void init() {
        Utility.InitMotors(hardwareMap, motors);
        shooter = hardwareMap.dcMotor.get("shooter");
        collector = hardwareMap.dcMotor.get("collector");
        //capballLift = hardwareMap.dcMotor.get("capballLift");
        hardwareController = new HardwareController(motors);
        rotations = shooter.getCurrentPosition();
    }

    @Override
    public void loop() {
        Move(gamepad1);
        if(!reseting) {
            SpinMotor(Leftpower(gamepad1), Leftpower(gamepad2), collector);
            SpinMotor(Rightpower(gamepad1), Rightpower(gamepad2), shooter);
        }
        //Sweep(gamepad1);
        resetMotors(gamepad1);
    }

    /**
     * If the A button is pressed the shooter is reset.
     * @param pad The gamepad used to control this action.
     */
    void resetMotors(Gamepad pad)
    {
        if(!reseting) {
            if (pad.a) {
                reseting = true;
            }
        }else{
            if(shooter.getCurrentPosition()%1400<rotations){
                shooter.setPower(-1.0);
            }else{
                reseting = false;
            }
        }
    }

    /**
     * Controls the motors using the gamepad.
     * @param pad The gamepad used to control this action.
     */
    void Move(Gamepad pad){
        hardwareController.RunMotors(pad.right_stick_x, -pad.right_stick_y, pad.left_stick_x);
    }

    /**
     * Returns a value based off of which button on the left side of the gamepad is pressed.
     * @param pad The gamepad used to control this action.
     * @return <code>1</code> if the left bumper is pressed,
     *         <code>-1</code> if the left trigger is pressed,
     *         <code>0</code> otherwise.
     */
    int Leftpower(Gamepad pad){
        if(pad.left_trigger>0.1){
            return -1;
        }else if(pad.left_bumper) {
            return 1;
        }
        return 0;
    }

    /**
     * Returns a value based off of which button on the right side of the gamepad is pressed.
     * @param pad The gamepad used to control this action.
     * @return <code>1</code> if the right bumper is pressed,
     *         <code>-1</code> if the right trigger is pressed,
     *         <code>0</code> otherwise.
     */
    int Rightpower(Gamepad pad){
        if(pad.right_trigger>0.1){
            return -1;
        }else if(pad.right_bumper) {
            return 1;
        }
        return 0;
    }

    /**
     * Controls a specific DcMotor based upon two inputs.
     * @param power1 A number based upon gamepad 1.
     * @param power2 A number based upon gamepad 2.
     * @param motor A DcMotor to spin based upon the powers.
     */
    void SpinMotor(int power1, int power2, DcMotor motor){
        if(power1==1||power1==-1){
            motor.setPower(power1);
        }else if(power2==1||power2==-1){
            motor.setPower(power2);
        }else{
            motor.setPower(0);
        }
    }

    /**
     * Control the sweeper of the robot.
     * @param pad The gamepad used to control this action.
     * @deprecated Replaced with {@link #SpinMotor(int, int, DcMotor)} in version 1.1.
     */
    void Sweep(Gamepad pad){

        if(collecting!=1&&pad.right_trigger>0.1&&collectSwitch){
            collector.setPower(-1.0);
            collecting = 1;
        }else if(collecting==1&&pad.right_trigger>0.1&&collectSwitch){
            collector.setPower(0.0);
            collecting = 0;
        }
        if(collecting!=-1&&pad.right_bumper&&collectSwitch){
            collector.setPower(1.0);
            collecting = -1;
        }else if(collecting==-1&&pad.right_bumper&&collectSwitch){
            collector.setPower(0.0);
            collecting = 0;
        }

        if(pad.right_bumper||pad.right_trigger>0.1){
            collectSwitch = false;
        }else{
            collectSwitch = true;
        }

//        if(pad.right_trigger>0.2){
//            collector.setPower(-1.0);
//        }else if(pad.right_bumper){
//            collector.setPower(1.0);
//        }else{
//            collector.setPower(0.0);
//        }
    }
}