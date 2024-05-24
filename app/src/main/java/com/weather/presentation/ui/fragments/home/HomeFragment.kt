package com.weather.presentation.ui.fragments.home

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.weather.BuildConfig
import com.weather.R
import com.weather.core.util.*
import com.weather.data.model.CityLocationDataItem
import com.weather.data.model.CurrentLocationData
import com.weather.data.model.WeatherHourlyCardData
import com.weather.data.model.nextweathermodel.hourlyweathers.HourlyWeatherModel
import com.weather.data.model.weathermodel.WeatherData
import com.weather.data.model.weathermodel.WeatherType
import com.weather.databinding.FragmentHomeBinding
import com.weather.presentation.adapter.HorizontalWeatherAdapter
import com.weather.presentation.ui.base.search.SearchActivity
import com.weather.presentation.ui.liveDate.SearchCitiesLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var weatherCardData: ArrayList<WeatherHourlyCardData>

    private lateinit var hourlyWeatherData: HourlyWeatherModel

    private lateinit var citiesDataObserver: Observer<CityLocationDataItem>


    private val mViewModel: WeatherViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setting data in UI
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
        binding.date.text = dateFormat.format(currentDate)

        binding.hourlyShimmerLayout.startShimmer()

        //First get location then set the location data in local and in UI after that get weather data from API
        initCurrentLocationThing()


        //Getting and setting data from SharedPref to UI
        var locationSharedPrefData = mViewModel.getSavedLocation()
        locationSharedPrefData?.let { setLocationDataToUI(it) }

        //Getting Data from Settings SharedPref
        setSettingDataToUI()


        //Observing the livedata from WeatherAPI
        mViewModel.weatherLiveData.observe(requireActivity()) {
            if(it != null) {
                setWeatherDataToUI(it.data!!)
                mViewModel.saveWeather(it.data)
            }
        }

        //Getting and setting data from SharedPref to UI
        val weatherData = mViewModel.getLocaleWeather()
        weatherData?.let { setWeatherDataToUI(it) }


        //Observing LiveData from API
        mViewModel.currentLocationLiveData.observe(requireActivity()) {
            if(it != null) {
                if(locationSharedPrefData == null || locationSharedPrefData!!.loc != it.data!!.loc && locationSharedPrefData!!.ip.isNotEmpty()) {
                    mViewModel.saveCurrentLocation(it.data!!)
                    locationSharedPrefData = it.data
                    setLocationDataToUI(it.data)
                    callingWeatherAPI(it.data)
                    callingHourlyWeatherAPI(it.data)
                }
            }
        }

        citiesDataObserver = Observer {
            val state = it.state ?: ""
            val data = CurrentLocationData(it.name, it.country, "", "${it.lat},${it.lon}", "", state, "")
            mViewModel.saveCurrentLocation(data)
            locationSharedPrefData = mViewModel.getSavedLocation()
            setLocationDataToUI(data)
            callingWeatherAPI(data)
            callingHourlyWeatherAPI(data)
        }
        SearchCitiesLiveData.getCitiesLiveData().observe(requireActivity(), citiesDataObserver)

        //Observing the livedata from API
        mViewModel.weatherByHourUseCaseLiveData.observe(requireActivity()) {
            if(it.data != null) {
                hourlyWeatherData = it.data
                changeWeatherToToday()
            }
        }

        callingAPIs(locationSharedPrefData)

        //On click "Search" icon
        binding.search.setOnClickListener {moveToSearch()}

        binding.next5days.setOnClickListener { locationSharedPrefData?.loc?.let { it1 ->
            navToUpcomingDaysFrag(
                it1
            )
        } }
        binding.today.setOnClickListener { changeWeatherToToday() }
        binding.tomorrow.setOnClickListener { changeWeatherToTomorrow() }
    }

    ///Calling APIs
    private fun callingAPIs(locationData: CurrentLocationData?){
        callingWeatherAPI(locationData)
        callingHourlyWeatherAPI(locationData)
    }
    //Calling "Hourly" Wither API
    private fun callingHourlyWeatherAPI(locationData: CurrentLocationData?) {
        val loc = locationData?.loc?.split(",")
        if(loc != null) {
            val lat = loc[0]
            val lon = loc[1]
            lifecycleScope.launch {
                if (InternetConnection.isNetworkAvailable(requireActivity())) {
                    lifecycleScope.launch {
                        mViewModel.getWeatherByHour(lat, lon, AppConstants.HOURLY_WEATHER_QUERY, AppConstants.HOURLY_WEATHER_CODE, AppConstants.HOURLY_WEATHER_DAYS_LIMIT, TimeZone.getDefault().id)
                    }
                } else {
                    val snackBar = Snackbar.make(requireView(), "No internet connection.", Snackbar.LENGTH_INDEFINITE)

                    snackBar.setAction(R.string.try_again) {
                        lifecycleScope.launch {
                            if (InternetConnection.isNetworkAvailable(requireActivity())) {
                                lifecycleScope.launch {
                                    mViewModel.getWeatherByHour(lat, lon, AppConstants.HOURLY_WEATHER_QUERY, AppConstants.HOURLY_WEATHER_CODE, AppConstants.HOURLY_WEATHER_DAYS_LIMIT, TimeZone.getDefault().id)
                                }
                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "No internet connection. Please try later.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }.show()
                }
            }
        }
    }

    //Calling "Main" weather API
    private fun callingWeatherAPI(locationData: CurrentLocationData?) {
        val loc = locationData?.loc?.split(",")
        if(loc != null) {
            val lat = loc[0]
            val lon = loc[1]

            lifecycleScope.launch {
                if (InternetConnection.isNetworkAvailable(requireActivity())) {
                    lifecycleScope.launch {
                        mViewModel.getWeather(lat, lon, BuildConfig.apiKey)
                    }
                } else {
                    val snackBar = Snackbar.make(
                        requireView(),
                        "No internet connection.",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackBar.setAction(R.string.try_again) {
                        lifecycleScope.launch {
                            if (InternetConnection.isNetworkAvailable(requireActivity())) {
                                lifecycleScope.launch {
                                    mViewModel.getWeather(lat, lon, BuildConfig.apiKey)
                                }
                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "No internet connection. Please try later.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }.show()
                }
            }
        }
    }



    //Set "City" data to UI
    private fun setLocationDataToUI(currentLocation: CurrentLocationData) {
        binding.cityName.text = currentLocation.city
        binding.countryName.text = CountryNameByCode.getCountryNameByCode(requireActivity(), currentLocation.country)
    }


    //Set "Main" weather data to UI
    private fun setWeatherDataToUI(weatherData: WeatherData) {
        var temp = weatherData.main.temp
        temp -= 273.15

        binding.atmosphericPressureValue.text = String.format("%.0f", weatherData.main.pressure.toDouble())


        val speed =weatherData.wind.speed*3.6
        binding.windValue.text = String.format("%.2f",speed)
//        binding.windValue.text = weatherData.wind.speed.toString()


        setWeatherIcon(weatherData.weather.first())

        binding.weatherNumericValue.text = String.format("%.0f", temp)
        binding.humidityValue.text = weatherData.main.humidity.toString()
        binding.weatherType.text = weatherData.weather.first().main
    }

    private fun setHourlyWeatherDataToUI(hourlyWeatherData: HourlyWeatherModel, isToday: Boolean): Int {
        val hourlyWeather = hourlyWeatherData.hourly

        val time = hourlyWeather.time
        val weather = hourlyWeather.temperature_2m
        val weatherCode = hourlyWeather.weathercode

        weatherCardData = ArrayList()

        var position = 0

        if(isToday) {
            for (i in 0 .. 23) {
                var currTime = TimeUtil.extractTimeFromString(time[i])
                val currWeather = weather[i]
                val weatherIcon = WeatherCodeToIcon.getWeatherIcon(weatherCode[i])
                val isCurrentTime = isCurrentLocalTime(currTime)

                if (isCurrentTime) {
                    currTime = "now"
                    position = i
                }

                val currTimeData = WeatherHourlyCardData(
                    currTime,
                    weatherIcon,
                    String.format("%.0f", currWeather),
                    isCurrentTime
                )

                weatherCardData.add(currTimeData)
            }

            return position
        } else {
            for (i in 24 .. 47) {
                val currTime = TimeUtil.extractTimeFromString(time[i])
                val currWeather = weather[i]
                val weatherIcon = WeatherCodeToIcon.getWeatherIcon(weatherCode[i])

                val currTimeData = WeatherHourlyCardData(
                    currTime,
                    weatherIcon,
                    String.format("%.0f", currWeather),
                    false
                )

                weatherCardData.add(currTimeData)
            }

            return 0
        }
    }


    private fun setSettingDataToUI() {
        var temp = binding.weatherNumericValue.text.toString().toDouble()

        binding.weatherUnit.text = resources.getString(R.string.celsius)
        temp = (temp - 32) * 5 / 9
        binding.weatherNumericValue.text = String.format("%.0f", temp)

        val pressure = binding.atmosphericPressureValue.text.toString().toDouble() * 1013.25
        binding.atmosphericPressureValue.text = String.format("%.0f", pressure)


        val prevWindUnit = binding.windUnit.text.toString()
        val prevWindSpeed = binding.windValue.text.toString().toDouble()


        val newWindSpeed: Double  = if(prevWindUnit == resources.getString(R.string.miles_per_hour)) prevWindSpeed * 1.60934  else prevWindSpeed * 3.6

        binding.windValue.text = String.format("%.2f", newWindSpeed)

    }

    //Get And Set weather icon to UI
    private fun setWeatherIcon(weatherType: WeatherType) {
        when (weatherType.id) {
            in 200..232 -> { //Thunderstorm
                binding.weatherIcon.setImageResource(R.drawable.icon_weather_thunderstorm_cloud) }
            in 300..321 -> { //Drizzle
                binding.weatherIcon.setImageResource(R.drawable.icon_weather_rain_cloud)
            }
            in 500..531 -> { //Rain
                binding.weatherIcon.setImageResource(R.drawable.icon_weather_sun_rain_cloud)
            }
            in 701..781 -> { //Atmosphere
                binding.weatherIcon.setImageResource(R.drawable.icon_weather_cloud_sun)
            }
            in 600..622 -> { //Snow
                binding.weatherIcon.setImageResource(R.drawable.icon_weather_snow_cloud)
            }
            800 -> { //Clear
                binding.weatherIcon.setImageResource(R.drawable.icon_weather_sun)
            }
            in 801..804 -> { //Cloud
                binding.weatherIcon.setImageResource(R.drawable.icon_weather_cloud)
            }
            else -> {
                binding.weatherIcon.setImageResource(R.drawable.icon_weather_sun)
            }
        }
    }



    //Get is time is current time
    private fun isCurrentLocalTime(timeString: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = Calendar.getInstance()
        val parsedTime = sdf.parse(timeString)

        if (parsedTime != null) {
            val parsedCalendar = Calendar.getInstance()
            parsedCalendar.time = parsedTime

            return currentTime.get(Calendar.HOUR_OF_DAY) == parsedCalendar.get(Calendar.HOUR_OF_DAY)
        }

        return false
    }

    private fun changeWeatherToToday() {
        binding.today.typeface = resources.getFont(R.font.inter_bold)
        binding.tomorrow.typeface = resources.getFont(R.font.inter)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.today.setTextColor(requireActivity().getColor(R.color.textColor))
        }
        binding.tomorrow.setTextColor(Color.parseColor("#D6996B"))

        changeIndicatorDotPosition(R.id.today)

        //Adding Today's Weather Data in array and adapter
        val position = setHourlyWeatherDataToUI(hourlyWeatherData, true)
        setHorizontalWeatherViewAdapter()

        binding.hourlyShimmerLayoutContainer.visibility = View.GONE
        binding.hourlyShimmerLayout.stopShimmer()
        binding.smallWeatherCardView.visibility = View.VISIBLE

        binding.smallWeatherCardView.layoutManager?.scrollToPosition(position)
    }

    private fun changeWeatherToTomorrow() {
        if(::hourlyWeatherData.isInitialized) {

            binding.today.typeface = resources.getFont(R.font.inter )
            binding.tomorrow.typeface = resources.getFont(R.font.inter_bold)



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tomorrow.setTextColor(requireActivity().getColor(R.color.textColor))
            }
            binding.today.setTextColor(Color.parseColor("#D6996B"))

            changeIndicatorDotPosition(R.id.tomorrow)

            //Adding Tomorrow's Weather Data in array and adapter
            setHourlyWeatherDataToUI(hourlyWeatherData, false)

            binding.hourlyShimmerLayoutContainer.visibility = View.GONE
            binding.hourlyShimmerLayout.stopShimmer()
            binding.smallWeatherCardView.visibility = View.VISIBLE

            setHorizontalWeatherViewAdapter()
        }
    }

    private fun changeIndicatorDotPosition(viewId: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.parentLayout)
        constraintSet.connect(R.id.indicator, ConstraintSet.START, viewId, ConstraintSet.START, 0)
        constraintSet.connect(R.id.indicator, ConstraintSet.END, viewId, ConstraintSet.END, 0)
        constraintSet.applyTo(binding.parentLayout)
    }

    private fun setHorizontalWeatherViewAdapter() {
        binding.smallWeatherCardView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)

        val adapter = HorizontalWeatherAdapter(
            weatherCardData
        )
        binding.smallWeatherCardView.adapter = adapter
    }


    //Open search activity
    private fun moveToSearch() {
        val intent = Intent(requireActivity(), SearchActivity::class.java)
        startActivity(intent)
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        } else {
            requireActivity().overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, R.anim.fadein, R.anim.fadeout)
        }
    }

    //Open upcoming days fragment
    private fun navToUpcomingDaysFrag(cord: String) {
        val loc = cord.split(",")
        val action =
            HomeFragmentDirections.navHomeFragToUpcomingDaysFrag(
                loc[0].trim(),
                loc[1].trim()
            )
        val navHelper = NavigateFragmentUtil()
        navHelper.navigateToFragmentWithAction(requireView(), action)
    }




    private fun initCurrentLocationThing() {

        //Calling API to get current location
        lifecycleScope.launch {
            if (InternetConnection.isNetworkAvailable(requireActivity())) {
                lifecycleScope.launch {
                    mViewModel.getCurrentLocation()
                }
            } else {
                val snackBar = Snackbar.make(requireView(), "No internet connection.", Snackbar.LENGTH_INDEFINITE)

                snackBar.setAction(R.string.try_again) {
                    lifecycleScope.launch {
                        if (InternetConnection.isNetworkAvailable(requireActivity())) {
                            lifecycleScope.launch { mViewModel.getCurrentLocation() }
                        } else {
                            Toast.makeText(requireActivity(), "No internet connection. Please try later.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.show()
            }
        }
    }




    override fun onDestroy() {
        super.onDestroy()
        SearchCitiesLiveData.getCitiesLiveData().removeObserver(citiesDataObserver)
    }

}