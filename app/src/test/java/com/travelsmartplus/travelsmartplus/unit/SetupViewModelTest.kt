package com.travelsmartplus.travelsmartplus.unit

import MainDispatcherRule
import com.google.android.material.textfield.TextInputEditText
import com.travelsmartplus.travelsmartplus.data.models.Airline
import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.Hotel
import com.travelsmartplus.travelsmartplus.data.models.requests.SetupAccountRequest
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.data.services.BookingService
import com.travelsmartplus.travelsmartplus.data.services.UserService
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.viewModels.SetupViewModel
import com.travelsmartplus.travelsmartplus.viewModels.UserViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class SetupViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val authService: AuthService = mockk()
    private val userService: UserService = mockk()
    private val bookingService: BookingService = mockk()
    private val sessionManager: SessionManager = mockk()
    private lateinit var viewModel: SetupViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { bookingService.getAllAirlines()} coAnswers {
            val mockedAirlines = mockk<List<Airline>>()
            Response.success(mockedAirlines)
        }
        coEvery { bookingService.getAllHotels()} coAnswers {
            val mockedHotels = mockk<List<Hotel>>()
            Response.success(mockedHotels)
        }
        viewModel = SetupViewModel(authService, userService, bookingService, sessionManager)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `passwordValidation should return true for valid passwords`() {

        // Mockk Data
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