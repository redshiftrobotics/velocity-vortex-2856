# 2856 Pull Request Format

## Guidelines
### Title
All pull requests should have a descriptive title, i.e., "Add new values for PID tuning"
### Description
This should give a brief overview of what you have/what you propose to do. You don't need any details here; you should outline the main points.
### Files Changed/Additions
Explain in moderate detail what additions and changes were made. Go beyond "changed file x," this is all visible in get. Explain what you changed/added, and why
## Sample Code
If applicable, sample code is always helpful. It will make it much easier to start using your new code right away. It doesn't have to be much, but demonstration of how to use various new classes/methods is always helpful.

--- 

## Example:

# Robot Data Config Files

## Functionality

- Functionality for a app-based structure for handling configuration of numerical values integral to the robot's function (i.e., values for PID tuning, motor speeds, etc.)
- This functionality is dynamic; it can be updated within opmodes, without rebuilding the application
- Supports both in-code configuration (i.e., setting of values through class methods, etc.), but can also parse JSON data

## Changes

- Added:
	- org/firstinspires/ftc/redshiftrobotics/config/ConfigVariable.java: Defines base class for configuration variables
	- org/firstinspires/ftc/redshiftrobotics/config/Config.java: defines base config class for list of all ConfigVariables
	- conf.json: example json config file, any config files should be written in this format
	- org/firstinspires/ftc/redshiftrobotics/config/testConfigVariables.java: test OpMode written in order to test Config Variables in the context of a running opmode
- Modified:
	- TeamCode/src/main/AndroidManifest.xml: permissions added to read files from the android filesystem. This is to allow the reading of config files.

## Example Usage

```java
...
import org.firstinspires.ftc.redshiftrobotics.config.Config;
import org.firstinspires.ftc.redshiftrobotics.config.ConfigVariable;

...
//This should go in a normal opmode
	public Config<Double> config = new Config<>(); //instantiate config class. Definition of config is "Config <E extends Number>," so template type argument must be a Number subclass.
	try {
		config.readFrom("/path/to/json/file"); //fill config file with data from json file
	} catch(Exception e) {
		e.printStackTrace();
		System.out.println("Error reading json"); //if there was a file reading or json exception
	}
	ConfigVariable<Double> confVariable = config.get("myvar")
	if (confVariable != null) { //make sure the variable is defined. This it mostly for testing/demonstrative purposes, unnecessary check if absolutely sure everything is correct.
		Double myvar = config.get("myvar").getValue();
	} else {
		System.out.println("Unable to get variable. Check config file...");
	}
	//do stuff with myvar if everything worked

```
