package com.example.app.hilt

import android.app.Application
import androidx.room.Room
import com.example.app.data.PasswordDb
import com.example.app.repo.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun provideDatabase(app: Application):PasswordDb{
        return Room.databaseBuilder(
                app,
                PasswordDb::class.java,
                "password_db"
            ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(db: PasswordDb): Repository {
        return Repository(db.passwordDao())
    }
}