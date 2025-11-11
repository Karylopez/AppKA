package com.example.eva2kari.data.repository

import com.example.eva2kari.data.local.UserDao
import com.example.eva2kari.data.model.LoginRequest
import com.example.eva2kari.data.model.User
import com.example.eva2kari.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    // API calls
    suspend fun login(username: String, password: String): Result<User> {
        return try {
            val response = RetrofitClient.apiService.login(
                LoginRequest(username, password)
            )

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                val user = User(
                    id = loginResponse.id,
                    username = loginResponse.username,
                    email = loginResponse.email,
                    firstName = loginResponse.firstName,
                    lastName = loginResponse.lastName,
                    gender = loginResponse.gender,
                    image = loginResponse.image,
                    token = loginResponse.token
                )
                // Guardar usuario en base de datos local
                userDao.insertUser(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUserFromApi(token: String): Result<User> {
        return try {
            val response = RetrofitClient.apiService.getCurrentUser("Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Database operations
    fun getCurrentUserFlow(): Flow<User?> = userDao.getCurrentUser()

    suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun updateUserImage(userId: Int, imagePath: String) {
        userDao.updateUserImage(userId, imagePath)
    }

    suspend fun deleteAllUsers() = userDao.deleteAllUsers()

    suspend fun logout() {
        userDao.deleteAllUsers()
    }
}