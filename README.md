# Weather App

This is a simple Android app that consumes the Open-Meteo API (https://open-meteo.com/en/docs) to display weather information.
The main focus 

## Features

- Material 3 design
- WorkManager for background work
- Navigation components for easy navigation flow
- Hilt for dependency injection
- Coroutines for asynchronous programming
- Retrofit for API consumption
- Gson for JSON parsing
- Room for local data storage
- Shimmer effect for loading indicators

## OverView

![ezgif com-video-to-gif(1)](https://user-images.githubusercontent.com/75399519/234094094-5a32f3eb-e4d6-44c5-a095-494c7f3b2ee9.gif)
![ezgif com-video-to-gif(2)](https://user-images.githubusercontent.com/75399519/234094340-c4bff397-8be2-4e9a-b223-dc141d7df0ee.gif)


## Skills Showcased

This project showcases my skills in using Android notifications and WorkManager. The app uses WorkManager to schedule background tasks for fetching weather data and updating the UI with notifications. The notifications provide users with up-to-date weather information without needing to open the app.

## Usage

You don't need any API key for the Open-Meteo API. 

## Details
WorkManager registers a work to displays the forecast of the last selected location every hour ( I kown it's not the most efficient interval but it's for debug purposes) 

