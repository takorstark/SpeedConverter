package controller;

import net.webservicex.*;

/**
 * Controller class that convert value from input unit to output unit.
 * 
 * @author Latthapat Tangtrustham 5510547014
 * @version 2014/11/04
 *
 */
public class SpeedConverterController {
	
	ConvertSpeeds cs = new ConvertSpeeds();
	ConvertSpeedsSoap css = cs.getConvertSpeedsSoap();
	
	/**
	 * Covert value from input unit to output unit
	 * 
	 * @param speed speed value
	 * @param fromUnit input unit
	 * @param toUnit output unit
	 * @return speed value in output unit
	 */
	public double convert(double speed, SpeedUnit fromUnit, SpeedUnit toUnit){
		return css.convertSpeed(speed, fromUnit, toUnit);
	}

	
	
	
}
