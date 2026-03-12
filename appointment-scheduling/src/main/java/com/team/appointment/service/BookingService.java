package com.team.appointment.service;

	

	import java.util.List;
	import com.team.appointment.model.AppointmentSlot;

	public class BookingService {
		private final BookingRuleStrategy durationRule = new DurationRule();
		private final BookingRuleStrategy capacityRule = new CapacityRule();

	    private static final int REQUIRED_DURATION = 30;
	  

	    private final SlotService slotService;

	    public BookingService(SlotService slotService) {
	        this.slotService = slotService;
	    }

	    public boolean bookAppointment(int index, int duration, int participants) {
	        List<AppointmentSlot> availableSlots = slotService.getAvailableSlots();

	        if (index < 1 || index > availableSlots.size()) {
	            return false;
	        }

	        if (duration != REQUIRED_DURATION) {
	            return false;
	        }

	        AppointmentSlot slot = availableSlots.get(index - 1);

	        if (!slot.canAcceptParticipants(participants)) {
	            return false;
	        }

	        if (slot.isBooked()) {
	            return false;
	        }
	        if (!durationRule.isValid(slot, participants)) {
	            return false;
	        }

	        if (!capacityRule.isValid(slot, participants)) {
	            return false;
	        }
	        slot.book(participants);
	        return true;
	    }
	    public boolean cancelBooking(int index) {
	        List<AppointmentSlot> allSlots = slotService.getAllSlots();

	        if (index < 1 || index > allSlots.size()) {
	            return false;
	        }

	        AppointmentSlot slot = allSlots.get(index - 1);

	        if (!slot.isBooked()) {
	            return false;
	        }

	        slot.cancel();
	        return true;
	    }

	    public boolean modifyBooking(int oldSlotNumber, int newSlotNumber, int duration, int participants) {

	        List<AppointmentSlot> allSlots = slotService.getAllSlots();

	        if (oldSlotNumber < 1 || oldSlotNumber > allSlots.size()) {
	            return false;
	        }

	        if (newSlotNumber < 1 || newSlotNumber > allSlots.size()) {
	            return false;
	        }

	        if (duration != REQUIRED_DURATION) {
	            return false;
	        }

	        AppointmentSlot oldSlot = allSlots.get(oldSlotNumber - 1);
	        AppointmentSlot newSlot = allSlots.get(newSlotNumber - 1);

	        if (!oldSlot.isBooked()) {
	            return false;
	        }

	        if (newSlot.isBooked()) {
	            return false;
	        }

	        if (!newSlot.canAcceptParticipants(participants)) {
	            return false;
	        }

	        // cancel old
	        oldSlot.cancel();

	        // book new
	        newSlot.book(participants);

	        return true;
	    }
	}
