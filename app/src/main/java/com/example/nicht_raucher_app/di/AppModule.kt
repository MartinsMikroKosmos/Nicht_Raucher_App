package com.example.nicht_raucher_app.di

import android.content.Context
import androidx.room.Room
import com.example.nicht_raucher_app.data.HabitDao
import com.example.nicht_raucher_app.data.HabitDatabase
import com.example.nicht_raucher_app.data.HabitRepositoryImpl
import com.example.nicht_raucher_app.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HabitDatabase {
        return Room.databaseBuilder(
            context,
            HabitDatabase::class.java,
            "habit_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideHabitDao(db: HabitDatabase): HabitDao = db.habitDao()

    @Provides
    @Singleton
    fun provideRepository(dao: HabitDao): Repository {
        return HabitRepositoryImpl(dao)
    }
}