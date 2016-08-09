package com.example.android.notepad.test;





import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import com.example.android.notepad.NotesList;
import com.example.android.notepad.util.ScreenShot;
import com.squareup.spoon.Spoon;

import eu.fbk.se.androidmonkey.Monkey;

import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.Display;

public class RandomTest extends ActivityInstrumentationTestCase2<NotesList> {
	
	private static final int NUM_EVENTS = 50;
	private static final String packageToTest = "com.example.android.notepad";
	
	
	
	public RandomTest(){
		super(packageToTest, NotesList.class);
	}

	/**
	 * Trigger the monkey tester
	 */
	public void testMonkeyEvents(){
		
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Instrumentation inst = getInstrumentation();
		PackageManager pm = getActivity().getPackageManager();
		
		Monkey monkey = new Monkey(display, packageToTest, inst, pm);
		
	    Context appcontext = inst.getTargetContext();
		Context context = inst.getContext();
		
		for (int i = 0; i < NUM_EVENTS; i++){
			monkey.nextRandomEvent();
			File screenshot = ScreenShot.screenshotpath(appcontext, "testNodePad_"+i, getClass().getName(), getName());
			Spoon.save(context, screenshot);
		}
		
	}
	
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		setActivityInitialTouchMode(false);
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
}
