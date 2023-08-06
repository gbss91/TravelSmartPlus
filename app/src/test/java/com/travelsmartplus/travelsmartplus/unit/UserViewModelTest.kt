package com.travelsmartplus.travelsmartplus.unit

import MainDispatcherRule
import com.google.android.material.textfield.TextInputEditText
import com.travelsmartplus.travelsmartplus.data.models.User
import com.travelsmartplus.travelsmartplus.data.models.requests.AddUserRequest
import com.travelsmartplus.travelsmartplus.data.models.requests.UpdatePasswordRequest
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.data.services.BookingService
import com.travelsmartplus.travelsmartplus.data.services.UserService
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import com.travelsmartplus.travelsmartplus.viewModels.UserViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val authService: AuthService = mockk()
    private val userService: UserService = mockk()
    private val sessionManager: SessionManager = mockk()
    private lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserViewModel(authService, userService, sessionManager)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `updateUser should update user`() = runTest {

        val userId = "1"
        val updatedUser = mockk<User>()

        coEvery { userService.updateUser(userId, updatedUser) } returns Response.success(updatedUser)

        viewModel.updateUser(userId, updatedUser)

        assertEquals(updatedUser, viewModel.userData.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `passwordUpdate should set passwordResponse when successful`() = runTest {

        val updatedPassword = mockk<UpdatePasswordRequest>()
        val response = Response.success(Unit)
        coEvery { authService.updatePassword(updatedPassword) } returns response

        viewModel.passwordUpdate(updatedPassword)

        assertEquals(response, viewModel.passwordResponse.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `deleteUser should set deleteUserResponse when successful`() = runTest {

        val userId = "1"
        val response = Response.success(Unit)
        coEvery { userService.deleteUser(userId) } returns response

        viewModel.deleteUser(userId)

        assertEquals(response, viewModel.deleteUserResponse.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `addUser should set userData when successful`() = runTest {

        val newUser = mockk<AddUserRequest>()
        val user = mockk<User>()
        coEvery { userService.addUser(newUser) } returns Response.success(user)

        viewModel.addUser(newUser)

        assertEquals(user, viewModel.userData.value)
        assertNull(viewModel.errorMessage.value)

    }


    @Test
    fun `editedDetailsValidation should return true for valid inputs`() {

        // Mockk Data
        val firstName = mockk<TextInputEditText>()
        val lastName = mockk<TextInputEditText>()
        val email = mockk<TextInputEditText>()

        every { firstName.text.toString() } returns "John"
        every { lastName.text.toString() } returns "Smith"
        every { email.text.toString() } returns "john@test.com"
        every { firstName.error } returns null
        every { lastName.error } returns null
        every { email.error } returns null

        val result = viewModel.editedDetailsValidation(firstName, lastName, email)

        assertTrue(result)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `editTravelDetailsValidation should return true for valid inputs`() {

        // Mockk Data
        val dob = mockk<TextInputEditText>()
        val passportNumber = mockk<TextInputEditText>()
        val expiryDate = mockk<TextInputEditText>()

        every { dob.text.toString() } returns "1991-01-01"
        every { passportNumber.text.toString() } returns "AB123456"
        every { expiryDate.text.toString() } returns "2026-01-01"
        every { dob.error } returns null
        every { passportNumber.error } returns null
        every { expiryDate.error } returns null

        val result = viewModel.editTravelDetailsValidation(dob, passportNumber, expiryDate)

        assertTrue(result)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `passwordValidation should return true for valid passwords`() {

        val password = mockk<TextInputEditText>()
        val confirmPass = mockk<TextInputEditText>()

        every { password.text.toString() } returns "Pass1234!"
        every { confirmPass.text.toString() } returns "Pass1234!"
        every { password.error } returns null
        every { confirmPass.error } returns null

        val result = viewModel.passwordValidation(password, confirmPass)

        assertTrue(result)
        assertNull(viewModel.errorMessage.value)
    }
}