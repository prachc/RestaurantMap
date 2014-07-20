package com.prach.mashup.restaurantmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class RestaurantMap extends Activity {
	private int[] ProcessNumber = null;
    private String[] ProcessName = null;
    private int current_pnum;
    
    private String address;
    private String station;
    
    public void debug(String msg){
		Log.d("com.prach.mashup.WAExtractor",msg);
	}
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
        ProcessNumber = getResources().getIntArray(R.array.process_number);
        ProcessName = getResources().getStringArray(R.array.process_name);
        current_pnum = -1;	
    }
    
	private void showInterruptDialog(String process, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Mashup Execution Interrupted");
		builder.setMessage("Relaunch current process ("+process+") or Quit this program ("+R.string.app_name+")");
		builder.setPositiveButton("Relaunch",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked Yes so do some stuff */
					}
				});
		builder.setNegativeButton("Quit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				});
		builder.show();
	}
	
	 private void showDialog(String title, String message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    
    @Override
    public void onResume(){
    	debug("onResume()");
    	super.onResume();
    	
    	Intent intent = null;
    	String URL = null;
    	String[] scripts = null;
    	int ocount = -1;
    	
    	switch (current_pnum) {
    	case -1:
    		intent = new Intent("com.prach.mashup.WAExtractor"); 
    		URL = "http://api.rakuten.co.jp/rws/3.0/rest?developerId=ca09e6d3a0c98fc5ac47598c0b9c2d02&operation=SimpleHotelSearch&version=2009-10-20&datumType=1&latitude=35.607243&longitude=139.686441&searchRadius=2";
    		ocount = 1;
    		scripts = new String[1];
    		scripts[0] = "var childElements = new Array();" +
    				"var tagArray = document.getElementsByTagName('nearestStation');" +
    				"for(var i=0;i<1;i++){" +
    				"	childElements[i] = tagArray[i].textContent;" +
    				"	window.prach.saveOutput(childElements[i],i);" +
    				"}" +
    				"window.prach.setfinishstate('true');";
    		intent.putExtra("URL", URL);
			intent.putExtra("SCRIPTS", scripts);
			intent.putExtra("OCOUNT", ocount);
	        startActivityForResult(intent, ProcessNumber[++current_pnum]);
			break;
    		
		case 0: //start : barcode reader
			intent = new Intent("com.prach.mashup.WAExtractor"); 
			URL = "http://dining.rakuten.co.jp/useragent/index?ua=pc";
			ocount = 1;
			//current_pnum++;
			scripts = new String[4];
			scripts[0] = "var tagArray1 = document.getElementsByTagName('form');"+
			"var parentElement;"+
			"for(var i=0;i<tagArray1.length;i++){"+
			"    if(i>=0&&i<6&&tagArray1[i].name=='search_box'){"+
			"        parentElement = tagArray1[i];"+
			"        break;"+
			"    }"+
			"}"+
			"var childElements = new Array();"+
			"var tagArray2 = parentElement.getElementsByTagName('input');"+
			"for(var i=0;i<tagArray2.length;i++)"+
			"    if(i==0&&tagArray2[i].id=='kw'&&tagArray2[i].name=='keyword')"+
			"        childElements[0] = tagArray2[i];"+
			"childElements[0].focus();"+
			"childElements[0].value = '"+station+"';"+
			"var tagArray2 = parentElement.getElementsByTagName('input');"+
			"for(var i=0;i<tagArray2.length;i++)"+
			"    if(i==1)"+
			"        childElements[1] = tagArray2[i];"+
			"childElements[1].click();";
			//----//----//----//----//----//----//----//----//----
			scripts[1] = "var tagArray1 = document.getElementsByTagName('div');"+
			"var parentElement;"+
			"for(var i=0;i<tagArray1.length;i++){"+
			"    if(i>=9&&i<17&&tagArray1[i].className=='mainInfo'){"+
			"        parentElement = tagArray1[i];"+
			"        break;"+
			"    }"+
			"}"+
			"var tagArray2 = parentElement.getElementsByTagName('a');"+
			"var childElement;"+
			"for(var i=0;i<tagArray2.length;i++)"+
			"    if(i==0)"+
			"        childElement = tagArray2[i];";
			//----//----//----//----//----//----//----//----//---- 
			scripts[2] = "var tagArray1 = document.getElementsByTagName('ul');"+
			"var parentElement;"+
			"for(var i=0;i<tagArray1.length;i++){"+
			"    if(i>=2&&i<10&&tagArray1[i].id=='tabMenu'&&tagArray1[i].className=='clearfix'){"+
			"        parentElement = tagArray1[i];"+
			"        break;"+
			"    }"+
			"}"+
			"var tagArray2 = parentElement.getElementsByTagName('img');"+
			"var childElement;"+
			"for(var i=0;i<tagArray2.length;i++)"+
			"    if(i==2)"+
			"        childElement = tagArray2[i];"+
			"window.location = childElement.parentElement.href;";
			//----//----//----//----//----//----//----//----//----
			scripts[3] = "var tagArray1 = document.getElementsByTagName('dl');"+
			"var parentElement;"+
			"for(var i=0;i<tagArray1.length;i++){"+
			"    if(i>=0&&i<9&&tagArray1[i].className=='clearfix'&&tagArray1[i].id==''){"+
			"        parentElement = tagArray1[i];"+
			"        break;"+
			"    }"+
			"}"+
			"var tagArray2 = parentElement.getElementsByTagName('dd');"+
			"var childElement;"+
			"for(var i=0;i<tagArray2.length;i++)"+
			"    if(i==0&&tagArray2[i].className=='first')"+
			"        childElement = tagArray2[i];"+
			"var RestaurantMap = childElement.textContent;"+
			"window.prach.saveOutput(RestaurantMap,0);" +
			"window.prach.setfinishstate('true');";
			//----//----//----//----//----//----//----//----//----
		
			intent.putExtra("URL", URL);
			intent.putExtra("SCRIPTS", scripts);
			intent.putExtra("OCOUNT", ocount);
	        startActivityForResult(intent, ProcessNumber[++current_pnum]);
			break;
		case 1: //start : barcode reader
			current_pnum++;
			String uri = "geo:0,0?q="+address;  
			startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
			
			break;
		default:
			break;
		} 
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		debug("onActivityResult()");
		if (requestCode == ProcessNumber[0]) {
			if (resultCode == RESULT_OK) {
				// String lat = intent.getStringExtra("LAT");
				// String lng = intent.getStringExtra("LNG");
				String[] outputs = intent.getStringArrayExtra("OUTPUTS");
				// String provider = intent.getStringExtra("PROVIDER");
				showDialog("WAE Data", outputs[0]);
				station = outputs[0];
			} else if (resultCode == RESULT_CANCELED) {
				showDialog("WAE canceled", "some msg");
			}
		} else if (requestCode == ProcessNumber[1]) {
			if (resultCode == RESULT_OK) {
				// String lat = intent.getStringExtra("LAT");
				// String lng = intent.getStringExtra("LNG");
				String[] outputs = intent.getStringArrayExtra("OUTPUTS");
				// String provider = intent.getStringExtra("PROVIDER");
				showDialog("WAE Data", outputs[0]);
				address = outputs[0];
			} else if (resultCode == RESULT_CANCELED) {
				showDialog("WAE canceled", "some msg");
			}
		}
	}
}