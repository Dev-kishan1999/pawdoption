package com.yadav.pawdoption.persistence

import com.yadav.pawdoption.model.Appointment
import com.yadav.pawdoption.model.BookedSlot
import com.yadav.pawdoption.model.Veterinarian

interface IAppointmentDAO {
    fun createSchedule(shelterId: String, vet: Veterinarian)
    fun bookAppointment(shelterId: String, vetId: String, vet: BookedSlot)
    fun createMyAppointment(userId: String, appointment: Appointment)
}