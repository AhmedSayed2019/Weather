package com.weather.presentation.ui.fragments.upcoming_days




import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.weather.R
import com.weather.core.util.*
import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeather
import com.weather.databinding.FragmentUpcomingDaysBinding
import com.weather.databinding.WeatherDayCardLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class UpcomingDaysFragment : Fragment() {

    private lateinit var binding: FragmentUpcomingDaysBinding

//    private lateinit var next5DaysWeatherViewModel: NextSevenDaysWeatherViewModel
//    private lateinit var upcomingDaysSharedPrefViewModel: UpcomingDaysSharedPrefViewModel
    private val mViewModel: UpcomingDaysViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: UpcomingDaysFragmentArgs by navArgs()
        val lat = args.lat
        val lon = args.lon


        callingNext5DaysWeatherAPI(lat, lon)

        val weatherData = getUpcomingWeatherSharedPrefData()

        if(weatherData != null) {
            setNext5DaysWeatherToUI(weatherData)
        }


        mViewModel.weatherByDayLiveData.observe(requireActivity()) {
            if(it != null) {
                setNext5DaysWeatherToUI(it.data!!.daily)
                sendDataToSharedPref(it.data.daily)
            }
        }

        binding.back.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun getUpcomingWeatherSharedPrefData(): NextDaysWeather? {
        return mViewModel.getLocaleWeatherByDay()
    }

    private fun callingNext5DaysWeatherAPI(lat: String, lon: String) {
        val dailyParameters = listOf("weathercode", "temperature_2m_max", "temperature_2m_min", "sunrise", "sunset")

        lifecycleScope.launch {
            if (InternetConnection.isNetworkAvailable(requireActivity())) {
                lifecycleScope.launch {
                    mViewModel.getWeatherByDay(lat, lon, dailyParameters, TimeZone.getDefault().id, AppConstants.NEXT5DAYS_WEATHER_DAYS_LIMIT)
                }
            } else {
                val snackBar =
                    Snackbar.make(requireView(), "No internet connection.", Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction(R.string.try_again) {
                    lifecycleScope.launch {
                        if (InternetConnection.isNetworkAvailable(requireActivity())) {
                            lifecycleScope.launch {
                                mViewModel.getWeatherByDay(lat, lon, dailyParameters, TimeZone.getDefault().id, AppConstants.NEXT5DAYS_WEATHER_DAYS_LIMIT)
                            }
                        } else {
                            Toast.makeText(requireActivity(), "No internet connection. Please try later.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.show()
            }
        }
    }

    private fun sendDataToSharedPref(weatherData: NextDaysWeather) {
        mViewModel.saveWeatherByDay(weatherData)
    }


    @SuppressLint("SetTextI18n")
    private  fun setDayUIData( view: WeatherDayCardLayoutBinding,index:Int ,weather: NextDaysWeather ){
        val weather1Icon = WeatherCodeToIcon.getWeatherIcon(weather.weathercode[index])

        try {
            view.weatherValue.text = "${ String.format("%.0f", weather.temperature_2m_min[index])} ${resources.getString(R.string.celsius)}"
            view.weatherIcon.setImageResource(weather1Icon)
            view.weatherDay.text = TimeUtil.convertDateStringToDay(weather.time[index])

        } catch (_: Exception) {

        }
    }
    private fun setNext5DaysWeatherToUI(weather: NextDaysWeather) {
        setDayUIData(view =  binding.weatherDay1, index = 2, weather = weather)
        setDayUIData(view =  binding.weatherDay2, index = 3, weather = weather)
        setDayUIData(view =  binding.weatherDay3, index = 4, weather = weather)
        setDayUIData(view =  binding.weatherDay4, index = 5, weather = weather)
        setDayUIData(view =  binding.weatherDay5, index = 6, weather = weather)
    }

}