package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by adam on 12/16/16.
 */
public class TimeData {
        private ElapsedTime ProgramTime;
        private float currentTime;

        public TimeData(){
            ProgramTime = new ElapsedTime();
        }

        public float now(){
            return (float) ProgramTime.seconds();
        }

        public float since(float PreviousTime){
            return (float) (ProgramTime.seconds() - PreviousTime);
        }

        public void updateCurrentTime() {
            this.currentTime = now();
        }

        public float getStoredTime() {
            return currentTime;
        }
}
