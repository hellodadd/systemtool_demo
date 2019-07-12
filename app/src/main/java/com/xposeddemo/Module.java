package com.xposeddemo;

import android.content.ContentResolver;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import com.system.android.sysoperation.ISysOperationHkLoadPackage;
import com.system.android.sysoperation.STool_MethodHk;
import com.system.android.sysoperation.STool_MethodHk.MethodHkParam;
import com.system.android.sysoperation.SToolSharedPref;
import com.system.android.sysoperation.SysOperationBridge;
import com.system.android.sysoperation.SysOperationHelpers;
import com.system.android.sysoperation.callbacks.STool_PackageLoad.LoadPackageParam;
import android.provider.Settings;
import android.content.Intent;
import android.os.BatteryManager;


public class Module implements ISysOperationHkLoadPackage {

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable
    {

		SysOperationHelpers.findAndHkMethod(
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
								SysOperationBridge.log("status");
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
			SysOperationHelpers.findAndHkMethod(hookClass,
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
			SysOperationBridge.log("Fake " + funcName + " ERROR: " + e.getMessage());
		}
	}
	
	
	
	
	public void FakeAndroidID_System(LoadPackageParam loadPkgParam,final String value) {
		try {
			SysOperationHelpers.findAndHkMethod("android.provider.Settings.System",
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
			SysOperationBridge.log("Fake Android ID ERROR: " + ex.getMessage());
		}
	}
	
	
	
	public void FakeAndroidID_Secure(LoadPackageParam loadPkgParam,final String value) {
		try {
			SysOperationHelpers.findAndHkMethod("android.provider.Settings.Secure",
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
			SysOperationBridge.log("Fake Android ID ERROR: " + ex.getMessage());
		}
	}
}
	

