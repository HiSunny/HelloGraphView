/**
 * This file is licensed under MIT
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Jonas Gehring
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.example.hellographview.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.example.hellographview.MainActivity;
import com.example.hellographview.R;

import java.util.Random;

/**
 * Created by jonas on 28.11.14.
 */
public class RealtimeUpdates extends Fragment implements SensorEventListener{
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer11;
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> mSeries1,mSeries11;
    private LineGraphSeries<DataPoint> mSeries2;
    private double graph2LastXValue1 = 5d;
    private double graph2LastXValue11 = 5d;
    private double graph2LastXValue2 = 5d;
    private float G = (float) 9.8;

    float[] accValues = new float[3];
    private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;
    Context mContext;
    public RealtimeUpdates(Context context){
    	this.mContext = context;
    	mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    	mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main2, container, false);

        GraphView graph1 = (GraphView) rootView.findViewById(R.id.graph);
        //mSeries1 = new LineGraphSeries<DataPoint>(generateData());
        //graph.addSeries(mSeries1);
        mSeries1 = new LineGraphSeries<DataPoint>();
        mSeries11 = new LineGraphSeries<DataPoint>();
        graph1.getLegendRenderer().setVisible(true);
		graph1.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);//右上角对每条线注释
        mSeries1.setTitle("accX[0]");
        mSeries11.setTitle("accY[1]");
        mSeries11.setColor(Color.RED);//线条颜色
        graph1.addSeries(mSeries1);
        graph1.addSeries(mSeries11);
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(40);

        GraphView graph2 = (GraphView) rootView.findViewById(R.id.graph2);
        mSeries2 = new LineGraphSeries<DataPoint>();
        mSeries2.setTitle("accZ[2]");
        graph2.getLegendRenderer().setVisible(true);
		graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);//右上角对每条线注释
        graph2.addSeries(mSeries2);
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(40);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
    }

    @Override
	public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        
        mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
       
        mTimer1 = new Runnable() {
            @Override
            public void run() {
               // mSeries1.resetData(generateData());
            	graph2LastXValue1 += 1d;
                mSeries1.appendData(new DataPoint(graph2LastXValue1, getRandom1()), true, 40);
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer1, 300);
    /////////////////////////////////////////////    
        mTimer11 = new Runnable() {
            @Override
            public void run() {
               // mSeries1.resetData(generateData());
            	graph2LastXValue11 += 1d;
                mSeries11.appendData(new DataPoint(graph2LastXValue11, getRandom11()), true, 40);
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer11, 300);
/////////////////////////////////////////////
        mTimer2 = new Runnable() {
            @Override
            public void run() {
                graph2LastXValue2 += 1d;
                mSeries2.appendData(new DataPoint(graph2LastXValue2, getRandom2()), true, 40);
                mHandler.postDelayed(this, 300);//600 500
            }
        };
        mHandler.postDelayed(mTimer2, 300);//900 700
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        mHandler.removeCallbacks(mTimer11);
        mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }

    /*  private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            //double f = mRand.nextDouble()*0.15+0.3;
             double y = accValues[1];//Math.sin(i*f+2) + mRand.nextDouble()*0.3;///可以改为加速度
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
           
            
        }
    	
        return values;
    }*/

    double mLastRandom1 = 0;//2;
    //Random mRand = new Random();
    private double getRandom1() {
        return mLastRandom1  = accValues[0] ;//+= mRand.nextDouble()*0.5 - 0.25;
    }//accValues[0]
   
    double mLastRandom11 = 0;//2;
    //Random mRand = new Random();
    private double getRandom11() {
        return mLastRandom11  = accValues[1] ;//+= mRand.nextDouble()*0.5 - 0.25;
    }//accValues[1]
    
    double mLastRandom2 = 0;//2;
    //Random mRand = new Random();
    private double getRandom2() {
        return mLastRandom2  = accValues[2] ;//+= mRand.nextDouble()*0.5 - 0.25;
    }//accValues[2]

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		accValues[0] = event.values[0];
		accValues[1] = event.values[1];
		accValues[2] = event.values[2] ;//- G ;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	
}
