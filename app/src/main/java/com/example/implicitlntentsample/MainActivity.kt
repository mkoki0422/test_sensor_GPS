package com.example.implicitlntentsample

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri

import android.os.Bundle
import android.os.Handler
import android.view.View


import android.widget.EditText
import android.widget.ImageView

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.example.flight_computer.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() ,SensorEventListener{

    var fAccell=FloatArray(3)
    var fMagnetic=FloatArray(3)
    var fAttitude=FloatArray(3)
    var cal_degreeDir=0f
    var muki_cal=0f

    private val mFirst = ArrayList<Float>()
    private val mSecond = ArrayList<Float>()
    private val mThrad = ArrayList<Float>()
    var sampleCount = 11 //サンプリング数 FASTEST31
    val param = FloatArray(3)

    private val gmFirst = ArrayList<Float>()
    private val gmSecond = ArrayList<Float>()
    private val gmThrad = ArrayList<Float>()
    var gsampleCount = 5 //サンプリング数 FASTEST11
    val gparam = FloatArray(3)


    private val smFirst = ArrayList<Float>()
    private val smSecond = ArrayList<Float>()
    private val smThrad = ArrayList<Float>()
    var ssampleCount = 21 //サンプリング数 FASTEST31
    val sparam = FloatArray(3)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event==null)return
        if(event.sensor.type==Sensor.TYPE_ACCELEROMETER){




            mFirst.add(event.values[0])
            mSecond.add(event.values[1])
            mThrad.add(event.values[2])

            //必要なサンプリング数に達したら
            if (mFirst.size == sampleCount) { //メディアンフィルタ(サンプリング数をソートして中央値を使用)かけて値を取得
//その値にさらにローパスフィルタをかける
                var lst = mFirst.clone() as ArrayList<Float>
                lst.sort()

                //Collections.sort(lst)
                param[0] = lst[5] * 0.9f + event.values[0] * 0.1f
                lst = mSecond.clone() as ArrayList<Float>
                //Collections.sort(lst)
                lst.sort()
                //Collections.sort(lst)
                param[1] = lst[5] * 0.9f + event.values[1] * 0.1f
                lst = mThrad.clone() as ArrayList<Float>
                lst.sort()
                param[2] = lst[5] * 0.9f + event.values[2] * 0.1f

                mFirst.clear()
                mSecond.clear()
                mThrad.clear()

                fAccell[0] = param[0]
                fAccell[1] = param[1]
                fAccell[2] = param[2]
                }

            var aa=String.format("%.4f",fAccell[0])
            var bb=String.format("%.4f",fAccell[1])
            var cc=String.format("%.4f",fAccell[2])
            kasokudo.text="X:${aa}Y:${bb}Z:${cc}"
        }

        if(event==null)return
        if(event.sensor.type==Sensor.TYPE_MAGNETIC_FIELD){



            gmFirst.add(event.values[0])
            gmSecond.add(event.values[1])
            gmThrad.add(event.values[2])

            //必要なサンプリング数に達したら
            if (gmFirst.size == gsampleCount) { //メディアンフィルタ(サンプリング数をソートして中央値を使用)かけて値を取得
//その値にさらにローパスフィルタをかける
                var glst = gmFirst.clone() as ArrayList<Float>
                glst.sort()
                gparam[0] = glst[2] * 0.9f + event.values[0] * 0.1f

                glst = gmSecond.clone() as ArrayList<Float>
                glst.sort()
                gparam[1] = glst[2] * 0.9f + event.values[1] * 0.1f

                glst = gmThrad.clone() as ArrayList<Float>
                glst.sort()
                gparam[2] = glst[2] * 0.9f + event.values[2] * 0.1f

                gmFirst.clear()
                gmSecond.clear()
                gmThrad.clear()

                fMagnetic[0] = gparam[0]
                fMagnetic[1] = gparam[1]
                fMagnetic[2] = gparam[2]

            }

            var a=String.format("%.4f",fMagnetic[0])
            var b=String.format("%.4f",fMagnetic[1])
            var c=String.format("%.4f",fMagnetic[2])
            ziki.text="X:${a}Y:${b}Z:${c}"
        }

        if(event==null)return
        if(event.sensor.type==Sensor.TYPE_PRESSURE){
            var z= ((Math.pow(((1013.25/event.values[0])),0.1902225604)-1)*(15+273.15)*504.7446153846)
            kiatu.text= kotlin.math.round(z).toString()

        }

        if(event==null)return
        if(event.sensor.type==Sensor.TYPE_LINEAR_ACCELERATION){

            smFirst.add(event.values[0])
            smSecond.add(event.values[1])
            smThrad.add(event.values[2])

            //必要なサンプリング数に達したら
            if (smFirst.size == ssampleCount) { //メディアンフィルタ(サンプリング数をソートして中央値を使用)かけて値を取得
//その値にさらにローパスフィルタをかける
                var lst = smFirst.clone() as ArrayList<Float>
                lst.sort()

                //Collections.sort(lst)
                sparam[0] = lst[10] * 0.9f + event.values[0] * 0.1f
                lst = smSecond.clone() as ArrayList<Float>
                //Collections.sort(lst)
                lst.sort()
                //Collections.sort(lst)
                sparam[1] = lst[10] * 0.9f + event.values[1] * 0.1f
                lst = smThrad.clone() as ArrayList<Float>
                lst.sort()
                sparam[2] = lst[10] * 0.9f + event.values[2] * 0.1f

                smFirst.clear()
                smSecond.clear()
                smThrad.clear()

            }

            var aaa=String.format("%.4f",sparam[0])
            var bbb=String.format("%.4f",sparam[1])
            var ccc=String.format("%.4f",sparam[2])
            syougeki.text="X:${aaa}Y:${bbb}Z:${ccc}"
            if (sparam[0]>100 || gparam[0]>100 || param[0]>100){
                check.text="aaaaaaaaaaa"
            }else{
                check.text=" "
            }





        }

      /*  if(event==null)return
        if(event.sensor.type==Sensor.TYPE_AMBIENT_TEMPERATURE){
            ondo.text= event.values[0].toString()
            Log.d("TAGU", event.values[0].toString())
        }*/

        if( fAccell != null && fMagnetic != null ) {
            var inR=FloatArray(9)
            SensorManager.getRotationMatrix(inR,null,fAccell,fMagnetic)

            var outR=FloatArray(9)
            SensorManager.remapCoordinateSystem(inR,SensorManager.AXIS_X,SensorManager.AXIS_Y,outR)

            SensorManager.getOrientation(outR,fAttitude)

            val doubleOrientation=(fAttitude[0]).toDouble()
            var degreeDir= Math.toDegrees(doubleOrientation).toFloat()
            if (degreeDir<0){
                cal_degreeDir=360+degreeDir
            }else{
                cal_degreeDir=degreeDir
            }
            houi_ziki.text=cal_degreeDir.toInt().toString()

            /*var rotate=RotateAnimation(0.0f, cal_degreeDir.toFloat(),(direction.width/2).toFloat(), (direction.height/2).toFloat())
            rotate.duration=1000
            startAnimation(rotate)
            
             */

        }


        val muki=findViewById<TextView>(R.id.muki)
        var cal=getDirection(_latitude,_longitude )
        muki_cal= (360-(cal_degreeDir-cal)).toFloat()
            if(muki_cal>360){
                muki.text=(muki_cal-360).toString()
            }else{
                muki.text=muki_cal.toString()
            }



    }





    var _latitude=0.0
    var _longitude=0.0
    var _altitude=0.0
    var _speed=0.0f
    var _houi=0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener=GPSLocationListener()






        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            val permissions= arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this@MainActivity,permissions,1000)

            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
        get_location_name(this,"鹿児島第一高校")

    }

    fun get_location_name(context: Context,name: String){
        var geocoder=Geocoder(context, Locale.getDefault())
        try {
            var location=geocoder.getFromLocationName(name,1)
            val addres=location[0]
            val lat=addres.latitude
            val lng=addres.longitude
            val adr = "$lat,$lng"
            place.text=adr
        }catch (e: Exception){
            place.text="えらー"
        }
    }


    override fun onResume() {
        super.onResume()
        val sensorManager=getSystemService(Context.SENSOR_SERVICE)as SensorManager

        val magnet=sensorManager.getDefaultSensor((Sensor.TYPE_MAGNETIC_FIELD))
        val accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val kiatu=sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        val syougeki=sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        val ondo=sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        sensorManager.registerListener(this,kiatu,SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this,magnet,SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this,syougeki,SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(this,ondo,SensorManager.SENSOR_DELAY_NORMAL)

        /*val handler= Handler()
        timer(period = 1000){
            val imageView=findViewById<ImageView>(R.id.imageView)
            val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.icons8__240);
            // bitmap画像をImgeViewに設定
            imageView.setImageBitmap(bitmap1)

            val image_width=bitmap1.width
            val image_height=bitmap1.height


            handler.post {
                var matrix= rotationMatrix(90f, image_width/2.toFloat(), image_height/2.toFloat())
                var bitmap=Bitmap.createBitmap(bitmap1,0,0,image_width,image_height,matrix,true)

                imageView.setImageBitmap(bitmap)


            }

        }

         */

        var handler=Handler()
        timer(period = 50){


            handler.post{
                animateRotation(imageView2,muki_cal)
                animateRotation(imageView3,360-cal_degreeDir)
            }
        }

    }






/*
/**
 * 場所名から緯度・経度を取得する。
 * @param context
 * @param 場所名
 * @return 緯度・経度の配列、または、null
 */
public static double[] getLocationFromPlaceName(Context context, String name) {
    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
    try {
        List&lt;Address&gt; location = geocoder.getFromLocationName(name, 1);
        if (location == null || location.size() &lt; 1) {
            return null;
        }

        Address address = location.get(0);
        double[] latlng = { address.getLatitude(), address.getLongitude() };
        return latlng;
    }
    catch (IOException e) {
        // 例外処理
        return null;
    }
}


 */

    private fun animateRotation(img: ImageView, direction :Float) {
        val objectAnimator = ObjectAnimator.ofFloat(img, "rotation", direction)
        objectAnimator.duration = 200
        objectAnimator.repeatCount = -1
        objectAnimator.start()

    }



    override fun onPause() {
        super.onPause()
        val sensorManager=getSystemService(Context.SENSOR_SERVICE)as SensorManager
        sensorManager.unregisterListener(this)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //ACCESS_FINE_LOCATIONに対するパーミションダイアログでかつ許可を選択したなら…
        if(requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //LocationManagerオブジェクトを取得。
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            //位置情報が更新された際のリスナオブジェクトを生成。
            val locationListener = GPSLocationListener()


            //再度ACCESS_FINE_LOCATIONの許可が下りていないかどうかのチェックをし、降りていないなら処理を中止。
            if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            //位置情報の追跡を開始。
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        }
    }


    fun onMapSearchButtonClick(view: View){
        val etSearchWord=findViewById<EditText>(R.id.etSearchWord)
        var searchWord=etSearchWord.text.toString()

        searchWord=URLEncoder.encode(searchWord,"UTF-8")
        val uriStr="geo:0,0?q=${searchWord}"
        val uri= Uri.parse(uriStr)
        val intent=Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)


    }







    fun onMapShowCurrentButtonClick(view:View){
        val uriStr="geo:${_latitude},${_longitude}"
        val uri=Uri.parse(uriStr)
        val intent=Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }

    private  inner class GPSLocationListener : LocationListener{
        override fun onLocationChanged(location : Location){
            _latitude=location.latitude
            _longitude=location.longitude



            _altitude=location.altitude*3.28084
            _speed = location.speed*3.6f

            _houi=location.bearing

            val tvLatitude=findViewById<TextView>(R.id.tvLatitude)
            tvLatitude.text=_latitude.toString()

            val tvLongitude=findViewById<TextView>(R.id.tvLongitude)
            tvLongitude.text=_longitude.toString()

            val altitude=findViewById<TextView>(R.id.altitude)
            altitude.text=_altitude.toString()

            val speed=findViewById<TextView>(R.id.speed)

                speed.text="${_speed}km/h"

            val houi=findViewById<TextView>(R.id.houi)
                houi.text=_houi.toString()


            //lat31.749379, lon130.7700304
            val daiichi=findViewById<TextView>(R.id.daiichi)
            daiichi.text=getDirection(_latitude,_longitude ).toString()

            val dictance=findViewById<TextView>(R.id.distace)
            dictance.text=getDistance(_latitude,_longitude,4).toString()

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }


    fun getDirection(
        latitude1: Double,
        longitude1: Double
        //latitude2: Double,
        //longitude2: Double
    ): Double {

        val lat1 = Math.toRadians(latitude1)
        val lat2 = Math.toRadians(21.389)
        val lng1 = Math.toRadians(longitude1)
        val lng2 = Math.toRadians(39.8579)
        val Y = Math.sin(lng2 - lng1) * Math.cos(lat2)
        val X =
            Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(
                lat2
            ) * Math.cos(lng2 - lng1)
        val deg = Math.toDegrees(Math.atan2(Y, X))
        var angle = (deg + 360) % 360
        angle=(Math.abs(angle) + 1 / 7200)
        return angle
    }


    fun getDistance(
        lat1: Double,
        lng1: Double,

        precision: Int
    ): Float {
        val R = 6371 // km
        val lat = Math.toRadians(21.389 - lat1)
        val lng = Math.toRadians(39.8579 - lng1)
        val A =
            Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(
                Math.toRadians(lat1)
            ) * Math.cos(Math.toRadians(21.389)) * Math.sin(lng / 2) * Math.sin(
                lng / 2
            )
        val C =
            2 * Math.atan2(Math.sqrt(A), Math.sqrt(1 - A))
        val decimalNo = Math.pow(10.0, precision.toDouble())
        var distance = R * C
        distance = Math.round(decimalNo * distance / 1) / decimalNo
        return distance.toFloat()
    }








}


