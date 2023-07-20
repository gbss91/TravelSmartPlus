package com.travelsmartplus.travelsmartplus.unit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.travelsmartplus.travelsmartplus.MainDispatcherRule
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.data.models.requests.BookingSearchRequest
import com.travelsmartplus.travelsmartplus.data.services.BookingService
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import io.mockk.MockK
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response

@ExperimentalCoroutinesApi
class BookingViewModelTest {

    private lateinit var viewModel: BookingViewModel
    private val bookingService: BookingService = mockk()
    private val sessionManager: SessionManager = mockk()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = BookingViewModel(bookingService, sessionManager)
    }

    @Test
    fun `bookingSearch should return update LiveData`() {

        // Given...
        val mockBooking = mockk<Booking>()
        val mockResponse = Response.success(mockBooking)
        val bookingSearchRequest = mockk<BookingSearchRequest>()
        val bookingService = mockk<BookingService>()
        val sessionManager = mockk<SessionManager>()
        val viewModel = BookingViewModel(bookingService, sessionManager)

        coEvery { bookingSearchRequest.userId = any() } just runs
        coEvery { bookingService.bookingSearch(any()) } returns mockResponse
        coEvery { sessionManager.currentUser() } returns 1

        // When...
        viewModel.bookingSearch()

        //Then...
        assertEquals(mockBooking, viewModel.booking.value)
        assertEquals(false, viewModel.isLoading.value)
    }

}