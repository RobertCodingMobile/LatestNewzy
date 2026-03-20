package com.robertcoding.data.di

import com.robertcoding.data.network.networkModule
import com.robertcoding.data.room.roomModule

val dataModule = repositoryModule + dataUtilsModule + networkModule + roomModule