package com.xposeddemo;

import android.content.ContentResolver;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import com.system.android.systemtool.ISystemToolHkLoadPackage;
import com.system.android.systemtool.STool_MethodHk;
import com.system.android.systemtool.STool_MethodHk.MethodHkParam;
import com.system.android.systemtool.SToolSharedPref;
import com.system.android.systemtool.SystemToolBridge;
import com.system.android.systemtool.SystemToolHelpers;
import com.system.android.systemtool.callbacks.STool_PackageLoad.LoadPackageParam;
import android.provider.Settings;
import android.content.Intent;
import android.os.BatteryManager;


public class Module implements ISystemToolHkLoadPackage {

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable
    {
		 
		SystemToolHelpers.findAndHkMethod(
                "android.content.Intent", 
                lpparam.classLoader,
                "getIntExtra", 
                String.class, 
                int.class, 
                new STool_MethodHk() {
                    @Override
                    protected void beforeHkedMethod(MethodHkParam param)
                            throws Throwable {
                        Intent intent = (Intent) param.thisObject;
                        final String action = intent.getAction();
                        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                            if (BatteryManager.EXTRA_LEVEL.equals(param.args[0] + "")) {
                                param.setResult(97);
                            } else if ("status".equals(param.args[0] + "")) {
                                SystemToolBridge.log("status");
                                param.setResult(BatteryManager.BATTERY_STATUS_NOT_CHARGING);
                            }
                        }
                    }

                    @Override
                    protected void afterHkedMethod(MethodHkParam param)
                        throws Throwable {
                    }
                }
         );
    }
        
    
	
	
	
	private void HookTelephony(String hookClass, LoadPackageParam loadPkgParam,
			String funcName, final String value) {
		try {
			SystemToolHelpers.findAndHkMethod(hookClass,
					loadPkgParam.classLoader, funcName, new STool_MethodHk() {

						@Override
						protected void afterHkedMethod(MethodHkParam param)
								throws Throwable {
							// TODO Auto-generated method stub
							super.afterHkedMethod(param);
							param.setResult(value);
						}

					});
			
		} catch (Exception e) {
			SystemToolBridge.log("Fake " + funcName + " ERROR: " + e.getMessage());
		}
	}
	
	
	
	
	public void FakeAndroidID_System(LoadPackageParam loadPkgParam,final String value) {
		try {
			SystemToolHelpers.findAndHkMethod("android.provider.Settings.System",
					loadPkgParam.classLoader, "getString",ContentResolver.class, String.class,
					new STool_MethodHk() {

				@Override
				protected void afterHkedMethod(MethodHkParam param)
						throws Throwable {
					
					if (param.args[1].equals(Settings.System.ANDROID_ID)) {
						param.setResult(value);
					}					
				}				
			});
			
		} catch (Exception ex) {
			SystemToolBridge.log("Fake Android ID ERROR: " + ex.getMessage());
		}
	}
	
	
	
	public void FakeAndroidID_Secure(LoadPackageParam loadPkgParam,final String value) {
		try {
			SystemToolHelpers.findAndHkMethod("android.provider.Settings.Secure",
					loadPkgParam.classLoader, "getString",
					ContentResolver.class, String.class, new STool_MethodHk() {

				@Override
				protected void afterHkedMethod(MethodHkParam param)
						throws Throwable {
					
					if (param.args[1].equals(Settings.Secure.ANDROID_ID)) {
						param.setResult(value);
					}					
				}				
			});
			
		} catch (Exception ex) {
			SystemToolBridge.log("Fake Android ID ERROR: " + ex.getMessage());
		}
	}
}
	

