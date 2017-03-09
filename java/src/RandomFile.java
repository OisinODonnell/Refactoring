/*
 * 
 * This class is for accessing, creating and modifying records in a file
 * 
 * */

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class RandomFile {
	private RandomAccessFile output;
	private RandomAccessFile input;
	private RandomAccessFile file = null;
	private RandomAccessEmployeeRecord record  = new RandomAccessEmployeeRecord();

	public void createFile(String fileName) {

		file = openFile(fileName, "rw", "Error processing file!" );

		if (file == null)
			System.exit(1);
		else
			closeFile();

	}

	public RandomAccessFile openFile(String fileName, String mode, String errMessage)
	{
		try {
			file = new RandomAccessFile(fileName, mode);
		}
		catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, errMessage);
		}

		return file;
	}

	public void openWriteFile(String fileName) {

		output = openFile(fileName, "rw", "File does not exist!" );

	}

	public void openReadFile(String fileName) {

		input = openFile(fileName, "r", "File is not supported!");

	}

	public void closeFile() {
		try
		{
			if (output != null)
				output.close();
		}
		catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error closing file!");
			System.exit(1);
		}
	}

	public long addRecords(Employee employeeToAdd) {
		Employee newEmployee = employeeToAdd;
		long currentRecordStart = 0;

		try // output values to file
		{
			record = new RandomAccessEmployeeRecord(newEmployee.getEmployeeId(), newEmployee.getPps(),
					newEmployee.getSurname(), newEmployee.getFirstName(), newEmployee.getGender(),
					newEmployee.getDepartment(), newEmployee.getSalary(), newEmployee.getFullTime());

			output.seek(output.length());// Look for proper position
			record.write(output);// Write object to file
			currentRecordStart = output.length();
		}
		catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error writing to file!");
		}
		return currentRecordStart - RandomAccessEmployeeRecord.SIZE;
	}

	public void changeRecords(Employee newDetails, long byteToStart) {
		long currentRecordStart = byteToStart;
		// object to be written to file

		Employee oldDetails = newDetails;
		try // output values to file
		{
			record = new RandomAccessEmployeeRecord(oldDetails.getEmployeeId(), oldDetails.getPps(),
					oldDetails.getSurname(), oldDetails.getFirstName(), oldDetails.getGender(),
					oldDetails.getDepartment(), oldDetails.getSalary(), oldDetails.getFullTime());

			output.seek(currentRecordStart);// Look for proper position
			record.write(output);// Write object to file
		}
		catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error writing to file!");
		}
	}

	public void deleteRecords(long byteToStart) {
		long currentRecordStart = byteToStart;

		try
		{
			record = new RandomAccessEmployeeRecord();// Create empty object
			output.seek(currentRecordStart);// Look for proper position
			record.write(output);// Replace existing object with empty object
		}
		catch (IOException ioException) {
			JOptionPane.showMessageDialog(null, "Error writing to file!");
		}
	}


	public long getFirstRecordPosition() {
		long byteToStart = 0;

		try {// try to get file
			input.length();
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO Exception found");
		}

		return byteToStart;
	}


	public long getLastRecordPosition() {
		long byteToStart = 0;

		try {// try to get position of last record
			byteToStart = input.length() - RandomAccessEmployeeRecord.SIZE;
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO Exception");
		}

		return byteToStart;
	}


	public long getNextRecordPosition(long readFrom) {
		long byteToStart = readFrom;

		try {
			input.seek(byteToStart);// Look for proper position in file
			// if next position is end of file go to start of file, else get next position
			if (byteToStart + RandomAccessEmployeeRecord.SIZE == input.length())
				byteToStart = 0;
			else
				byteToStart = byteToStart + RandomAccessEmployeeRecord.SIZE;
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Number Format Exception Found");
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO Exception");
		}
		return byteToStart;
	}


	public long getPreviousRecordPosition(long readFrom) {
		long byteToStart = readFrom;

		try {
			input.seek(byteToStart);// Look for proper position in file
			// if previous position is start of file go to end of file, else get previous position
			if (byteToStart == 0)
				byteToStart = input.length() - RandomAccessEmployeeRecord.SIZE;
			else
				byteToStart = byteToStart - RandomAccessEmployeeRecord.SIZE;
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Number Format Exception");
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO Exception");
		}
		return byteToStart;
	}


	public Employee readRecordAtPosition(long byteToStart) {
		Employee thisEmp = null;

		try {// try to read file and get record
			input.seek(byteToStart);// Look for proper position in file
			record.read(input);// Read record from file
		} // end try
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO Exception");
		}// end catch

		thisEmp = record;

		return thisEmp;
	}

	// Check if PPS Number already in use
	public boolean isPpsExist(String pps, long currentByteStart) {

		boolean ppsExist = false;
		long oldByteStart = currentByteStart;
		long currentByte = 0;

		try {// try to read from file and look for PPS Number
			// Start from start of file and loop until PPS Number is found or search returned to start position
			while (currentByte != input.length() && !ppsExist) {
				//if PPS Number is in position of current object - skip comparison
				if (currentByte != oldByteStart) {
					input.seek(currentByte);// Look for proper position in file
					record.read(input);// Get record from file
					// If PPS Number already exist in other record display message and stop search
					if (record.getPps().trim().equalsIgnoreCase(pps)) {
						ppsExist = true;
						JOptionPane.showMessageDialog(null, "PPS number already exist!");
					}
				}
				currentByte = currentByte + RandomAccessEmployeeRecord.SIZE;
			}
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO Exception");
		}

		return ppsExist;
	}

	// Check if any record contains valid ID - greater than 0
	public boolean isSomeoneToDisplay() {
		boolean someoneToDisplay = false;
		long currentByte = 0;

		try {// try to read from file and look for ID
			// Start from start of file and loop until valid ID is found or search returned to start position
			while (currentByte != input.length() && !someoneToDisplay) {
				input.seek(currentByte);
				record.read(input);
				// If valid ID exist in stop search
				if (record.getEmployeeId() > 0)
					someoneToDisplay = true;
				currentByte = currentByte + RandomAccessEmployeeRecord.SIZE;
			}
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "IO Exception");
		}

		return someoneToDisplay;
	}
}