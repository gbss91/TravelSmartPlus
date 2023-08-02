package com.travelsmartplus.travelsmartplus.unit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.travelsmartplus.travelsmartplus.data.models.Airport
import com.travelsmartplus.travelsmartplus.data.models.Booking
import com.travelsmartplus.travelsmartplus.data.models.FlightBooking
import com.travelsmartplus.travelsmartplus.data.models.requests.BookingSearchRequest
import com.travelsmartplus.travelsmartplus.data.services.BookingService
import com.travelsmartplus.travelsmartplus.data.services.BookingServiceImpl
import com.travelsmartplus.travelsmartplus.utils.SessionManager
import com.travelsmartplus.travelsmartplus.viewModels.BookingViewModel
import io.mockk.Awaits
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response

@ExperimentalCoroutinesApi
class BookingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val bookingService: BookingService = mockk()
    private val sessionManager: SessionManager = mockk()
    private lateinit var viewModel: BookingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { bookingService.getAllAirports() } coAnswers {
            val mockedAirports = mockk<List<Airport>>()
            Response.success(mockedAirports)
        }
        viewModel = BookingViewModel(bookingService, sessionManager)

    }

    @Test
    fun `airportsAutoComplete success returns all airports`() = runTest {

        val airportsList = mockk<List<Airport>>()
        coEvery { bookingService.getAllAirports() } returns Response.success(airportsList)

        viewModel.airportsAutoComplete()

        assertEquals(airportsList, viewModel.airports.value)
    }

    @Test
    fun `bookingSearch returns booking`() = runTest {

        val bookingSearchRequest = mockk<BookingSearchRequest>()
        val booking = mockk<Booking>()
        every { bookingSearchRequest.userId } returns 1
        coEvery { sessionManager.currentUser() } returns 1
        coEvery { bookingService.bookingSearch(bookingSearchRequest) } returns Response.success(booking)

        viewModel.bookingSearch()

        assertEquals(booking, viewModel.booking.value)
    }

    @Test
    fun `getFlightOffers returns list of flights`() =  runTest {

        val bookingSearchRequest = mockk<BookingSearchRequest>()
        val flightOffers = mockk<List<FlightBooking>>()
        every { bookingSearchRequest.userId } returns 1
        coEvery { sessionManager.currentUser() } returns 1
        coEvery { bookingService.getFlightOffers(bookingSearchRequest) } returns Response.success(flightOffers)

        // Act
        viewModel.setBookingSearchRequest(bookingSearchRequest)
        viewModel.getFlightOffers()
        advanceUntilIdle()

        // Assert
        assertEquals(flightOffers, viewModel.flightOffers.value)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }
}
