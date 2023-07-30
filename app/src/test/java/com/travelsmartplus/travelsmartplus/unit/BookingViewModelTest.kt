package com.travelsmartplus.travelsmartplus.unit

import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.data.models.requests.BookingSearchRequest
import com.travelsmartplus.travelsmartplus.data.services.BookingService
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class BookingViewModelTest {

    private lateinit var viewModel: BookingViewModel
    private val bookingService: BookingService = mockk()
    private val sessionManager: SessionManager = mockk()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = BookingViewModel(bookingService, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `bookingSearch should return updated LiveData`() = runBlocking {

        // Given...
        val mockBooking = mockk<Booking>()
        val mockResponse = Response.success(mockBooking)
        val mockAirports = Response.success(mockk<List<Airport>>())
        val bookingSearchRequest = mockk<BookingSearchRequest>()
        val bookingService = mockk<BookingService>()
        val sessionManager = mockk<SessionManager>()
        val viewModel = BookingViewModel(bookingService, sessionManager)

        coEvery { bookingService.getAllAirports() } returns mockAirports
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