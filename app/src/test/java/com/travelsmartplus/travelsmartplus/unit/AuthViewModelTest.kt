package com.travelsmartplus.travelsmartplus.unit

import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.requests.SignInRequest
import com.travelsmartplus.travelsmartplus.data.models.responses.AuthResponse
import com.travelsmartplus.travelsmartplus.data.services.AuthService
import com.travelsmartplus.travelsmartplus.data.services.BookingService
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.viewModels.AuthViewModel
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val authService: AuthService = mockk()
    private val viewModel: AuthViewModel = AuthViewModel(authService)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `signIn should update session`() = runTest {

        val signInRequest = mockk<SignInRequest>()
        val signInResponse = mockk<Response<AuthResponse>>()

        coEvery { authService.signIn(signInRequest) } returns signInResponse

        viewModel.signIn(signInRequest)
        advanceUntilIdle()

        Assert.assertEquals(signInResponse, viewModel.signInResponse.value)

    }

    @Test
    fun `signUpValidation returns true when all fields are valid`() = runTest  {

        // Mock data
        val firstName = mockk<TextInputEditText>()
        val lastName = mockk<TextInputEditText>()
        val email = mockk<TextInputEditText>()
        val companyName = mockk<TextInputEditText>()
        val duns = mockk<TextInputEditText>()
        val password = mockk<TextInputEditText>()
        val confirmPass = mockk<TextInputEditText>()

        every { firstName.text.toString() } returns "John"
        every { lastName.text.toString() } returns "Smith"
        every { email.text.toString() } returns "john@test.com"
        every { companyName.text.toString() } returns "Company XYZ"
        every { duns.text.toString() } returns "123456789"
        every { password.text.toString() } returns "Pass1234!"
        every { confirmPass.text.toString() } returns "Pass1234!"

        every { firstName.error } returns null
        every { lastName.error } returns null
        every { email.error } returns null
        every { companyName.error } returns null
        every { duns.error } returns null
        every { password.error } returns null
        every { confirmPass.error } returns null

        val result = viewModel.signUpValidation(
            firstName,
            lastName,
            email,
            companyName,
            duns,
            password,
            confirmPass
        )

        assertTrue(result)
    }

    @Test
    fun `signInValidation should return true when email and password are valid`() = runTest  {

        // Mock data
        val email = mockk<TextInputEditText>()
        val password = mockk<TextInputEditText>()

        every { email.text.toString() } returns "john@test.com"
        every { password.text.toString() } returns "Pass1234!"

        every { email.error } returns null
        every { password.error } returns null

        val result = viewModel.signInValidation(email, password)

        assertTrue(result)

    }
}