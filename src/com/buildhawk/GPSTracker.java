package com.buildhawk;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * Fetches Last location coordinates of the device by either GPS or Network
 * 
 * @author shankar
 * 
 */
public class GPSTracker extends Service implements LocationListener {

	private final Context mContext;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	private static boolean dialogShown = false;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

	/**
	 * Initializes Tracker Class
	 * 
	 * @param context
	 */
	public GPSTracker(Context context) {
		this.mContext = context;
		start(context);
		// getLocation();
	}

	/**
	 * Fetches last location
	 * 
	 * @return Location Object
	 */
	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				Log.e("no network provider is enabled",
						"no network provider is enabled");
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.e("Network", "Network");
					if (locationManager != null) {
						Log.e("if", "if");
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							Log.e("if", "if");
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						} else {
							Log.e("else", "else");
						}
					} else {
						Log.e("else", "else");
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.e("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							Log.e("if", "if");
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								Log.e("if", "if");
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							} else {
								Log.e("else", "else");
							}
						} else {
							Log.e("else", "else");
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Start receiving location updates
	 */
	public void start(final Context context) {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				Log.e("no network provider is enabled",
						"no network provider is enabled");
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.e("Network", "Network");
					if (locationManager != null) {
						Log.e("if", "if");
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							Log.e("if", "if");
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						} else {
							Log.e("else", "else");
						}
					} else {
						Log.e("else", "else");
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.e("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							Log.e("if", "if");
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								Log.e("if", "if");
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							} else {
								Log.e("else", "else");
							}
						} else {
							Log.e("else", "else");
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * Stop using GPS listener Calling this function will stop using GPS in your
	 * application
	 * */
	public void stop() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
		this.stopSelf();
	}

	/**
	 * Stop using GPS listener Calling this function will stop using GPS in your
	 * application
	 * */
	public void destroy() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
		this.stopSelf();
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * 
	 * @return boolean
	 * */
	public boolean isLocationEnabled(Context context) {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	public void showSettingsAlert(final Context mContext) {
		try {
			if (!dialogShown) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						mContext);

				// Setting Dialog Title
				alertDialog.setTitle("Loaction Settings");

				// Setting Dialog Message
				alertDialog
						.setMessage("Location is not enabled. Do you want to go to settings menu?");

				// On pressing Settings button
				alertDialog.setPositiveButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								mContext.startActivity(intent);
								dialog.dismiss();
								dialogShown = false;
							}
						});

				// on pressing cancel button
				alertDialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								dialogShown = false;
							}
						});

				// Showing Alert Message
				alertDialog.show();
				dialogShown = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			this.location = location;
			Log.e("onLocationChanged", "=" + location);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.e("onProviderDisabled", "=" + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.e("onProviderEnabled", "=" + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.e("onStatusChanged", "= provider => " + provider + " , status => "
				+ status + " , extras => " + extras);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.e("onBind", "=" + arg0);
		return null;
	}

}
