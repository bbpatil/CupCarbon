package senscript;

import arduino.Bracket;
import device.SensorNode;
import wisen_simulation.SimLog;

public class Command_WAIT extends Command {
	
	protected String arg = "";
	
	public Command_WAIT(SensorNode sensor) {
		this.sensor = sensor ;
	}
	
	public Command_WAIT(SensorNode sensor, String arg) {
		this.sensor = sensor ;
		this.arg = arg;
	}

	@Override
	public double execute() {		
		double event = 0 ;

		if (sensor.dataAvailable()) {			
			SimLog.add("S" + sensor.getId() + " Buffer available, exit waiting.");
			sensor.getScript().setWaiting(false);
			return 0 ;
		} 
		else {
			SimLog.add("S" + sensor.getId() + " is waiting for data ...");			
			sensor.getScript().setWaiting(true);			
			if (arg.equals(""))
				event = Double.MAX_VALUE;
			else {
				event = (Double.parseDouble(sensor.getScript().getVariableValue(arg))/1000.);
			}
		}
		
		return event;
	}

	@Override
	public boolean isWait() {
		return true;
	}
	
	@Override
	public String getArduinoForm() {
		Bracket.n++;
		Bracket.n++;
		
		String s = "";
		s += "\t" + "xbee.readPacket("+arg+");\n";
		s += "\tif (xbee.getResponse().isAvailable()) {\n";
		s += "\tif (xbee.getResponse().getApiId() == RX_64_RESPONSE) {\n";
		return s ;
	}

	@Override
	public String toString() {
		return "WAIT";
	}

	@Override
	public String finishMessage() {
		return ("S" + sensor.getId() + " has finished waiting.");
	}
	
}
