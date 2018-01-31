
package unittests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import client.ClientMock;
import client.IMessageReceiveHandler;
import controllers.CostumerCancelReservation;
import entities.Costumer;
import entities.EntitiesEnums.ReservationDeliveryType;
import entities.EntitiesEnums.ReservationType;
import entities.EntitiesEnums.UserPrivilege;
import entities.EntitiesEnums.UserStatus;
import entities.IEntity;
import entities.Reservation;
import messages.EntityData;
import messages.IMessageData;
import messages.Message;
import messages.MessageFactoryStub;
import messages.RespondMessageData;

/**
 *
 * CostumerCancelReservationTest: Testing class for the reservation canceling.
 * 
 */
public class CostumerCancelReservationTest
{

	private ClientMock m_clientShame;

	private CostumerCancelReservation m_cancelReservation;

	private List<Reservation> m_reservationList;

	private Costumer m_costumer;

	/**
	 * Set up the testing class.
	 *
	 * @throws Exception
	 *             An exception.
	 */
	@Before
	public void setUp() throws Exception
	{
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		m_reservationList = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			Reservation reservation = new Reservation();
			reservation.setId(i);
			reservation.setBlessingCard("");
			reservation.setCostumerId(1);
			reservation.setCreditCard("");
			reservation.setDeliveryAddress("");
			calendar.add(Calendar.MINUTE, 50);
			reservation.setDeliveryDate(calendar.getTime());
			reservation.setDeliveryName("");
			reservation.setDeliveryPhone("");
			reservation.setDeliveryType(ReservationDeliveryType.Future);
			reservation.setNumberOfItems(i * 2);
			reservation.setPrice(100);
			reservation.setShopManagerId(1);
			reservation.setType(ReservationType.Open);
			m_reservationList.add(reservation);
		}

		m_costumer = new Costumer();
		m_costumer.setId(1);
		m_costumer.setBalance(0);
		m_costumer.setEmail("");
		m_costumer.setPassword("123");
		m_costumer.setPrivilege(UserPrivilege.Costumer);
		m_costumer.setStatus(UserStatus.Connected);
		m_costumer.setUserName("costumer");

		m_clientShame = new ClientMock(m_reservationList, m_costumer);
		m_cancelReservation = new CostumerCancelReservation(m_clientShame, new MessageFactoryStub());
	}

	/**
	 * Testing method to test if a reservation cancellation is delivered to the
	 * server and updated successfully.
	 */
	@Test
	public void testCancelReservation()
	{
		m_clientShame.setMessagesHandler((msg) -> {
			/* Parse the response from the mock. */
			IMessageData messageData = msg.getMessageData();
			RespondMessageData respondMessageData = (RespondMessageData) messageData;
			IEntity entity = ((EntityData) respondMessageData.getMessageData()).getEntity();
			if (entity instanceof Costumer) {
				assertTrue("The costumer was not updated.", respondMessageData.isSucceed());
			} else if (entity instanceof Reservation) {
				Reservation reservation = (Reservation) entity;
				assertTrue("The reservation was not updated.",
						respondMessageData.isSucceed() && reservation.getType() == ReservationType.Canceled);
			}
		});
		Reservation reservation = m_reservationList.get(0);
		assertTrue("Failed to send reservation cancel. Reservation: " + reservation,
				m_cancelReservation.cancelReservation(m_costumer, reservation));
	}

	/**
	 * Testing method to test if a reservation with wrong reservation ID is
	 * delivered to the server and returns false for the update request.
	 */
	@Test
	public void testCancelReservationIdNotExist()
	{
		m_clientShame.setMessagesHandler((msg) -> {
			/* Parse the response from the mock. */
			IMessageData messageData = msg.getMessageData();
			RespondMessageData respondMessageData = (RespondMessageData) messageData;
			IEntity entity = ((EntityData) respondMessageData.getMessageData()).getEntity();
			if (entity instanceof Reservation) {
				assertFalse("The reservation with wrong ID was accepted.", respondMessageData.isSucceed());
			}
		});
		/* Create reservation instance with not existing ID */
		Reservation reservation = new Reservation(m_reservationList.get(0));
		reservation.setId(10);
		assertTrue("Failed to send reservation cancel. Reservation: " + reservation,
				m_cancelReservation.cancelReservation(m_costumer, reservation));
	}

	/**
	 * Testing method to test if a reservation with wrong costumer ID is delivered
	 * to the server and returns false for the update request.
	 */
	@Test
	public void testCancelReservationCostumerIdNotExist()
	{
		m_clientShame.setMessagesHandler((msg) -> {
			/* Parse the response from the mock. */
			IMessageData messageData = msg.getMessageData();
			RespondMessageData respondMessageData = (RespondMessageData) messageData;
			IEntity entity = ((EntityData) respondMessageData.getMessageData()).getEntity();
			if (entity instanceof Reservation) {
				assertFalse("The reservation with wrong costumer ID was accepted.", respondMessageData.isSucceed());
			}
		});
		/* Create reservation instance with not existing costumer ID */
		Reservation reservation = new Reservation(m_reservationList.get(2));
		reservation.setCostumerId(2);
		assertTrue("Failed to send reservation cancel. Reservation: " + reservation,
				m_cancelReservation.cancelReservation(m_costumer, reservation));
	}

	/**
	 * Testing method to test if a reservation with wrong shop ID is delivered to
	 * the server and returns false for the update request.
	 */
	@Test
	public void testCancelReservationShopIdNotExist()
	{
		m_clientShame.setMessagesHandler((msg) -> {
			/* Parse the response from the mock. */
			IMessageData messageData = msg.getMessageData();
			RespondMessageData respondMessageData = (RespondMessageData) messageData;
			IEntity entity = ((EntityData) respondMessageData.getMessageData()).getEntity();
			if (entity instanceof Reservation) {
				assertFalse("The reservation with wrong shop ID was accepted.", respondMessageData.isSucceed());
			}
		});
		/* Create reservation instance with not existing shop ID */
		Reservation reservation = new Reservation(m_reservationList.get(2));
		reservation.setShopManagerId(2);
		assertTrue("Failed to send reservation cancel. Reservation: " + reservation,
				m_cancelReservation.cancelReservation(m_costumer, reservation));
	}

	/**
	 * Testing method to test if null data is delivered to the canceling method.
	 */
	@Test
	public void testCancelReservationSendNullData()
	{
		/* Check for sending null data instead of reservation. */
		assertFalse("Successfully delivered null data.", m_cancelReservation.cancelReservation(m_costumer, null));

		/* Check for sending null data instead of costumer. */
		assertFalse("Successfully delivered null data.",
				m_cancelReservation.cancelReservation(null, m_reservationList.get(1)));

		/* Check for sending null data instead of reservation and costumer. */
		assertFalse("Successfully delivered null data.", m_cancelReservation.cancelReservation(null, null));
	}

	/**
	 * Testing method to test if the refund calculating method return the right
	 * value.
	 */
	@Test
	public void testCalculateRefund()
	{
		/* Check the method for less than a hour date before the delivery date. */
		float expectedRefund = 0;
		assertTrue("Refund for less than a hour calculated wrongly.",
				m_cancelReservation.calculateRefund(m_reservationList.get(0)) == expectedRefund);

		/*
		 * Check the method for less than 3 hours date before the delivery date and more
		 * than a hour.
		 */
		expectedRefund = 50;
		assertTrue("Refund for less than 3 hours and more than a hour calculated wrongly.",
				m_cancelReservation.calculateRefund(m_reservationList.get(2)) == expectedRefund);

		/* Check the method for more than 3 hours date before the delivery date. */
		expectedRefund = 100;
		assertTrue("Refund for more than 3 hour calculated wrongly.",
				m_cancelReservation.calculateRefund(m_reservationList.get(3)) == expectedRefund);
	}

	/**
	 * Testing method to test if the refund calculating method throws
	 * {@link RuntimeException} when sending negative price in the reservation.
	 */
	@Test
	public void testCalculateRefundNegativePrice()
	{
		Reservation reservation = new Reservation(m_reservationList.get(1));
		reservation.setPrice(-8);
		try {
			m_cancelReservation.calculateRefund(reservation);
			/* If the method returns here then no exception was thrown. */
			fail("The reservation with negative price doesn't throw exception as axpected.");
		}
		catch (RuntimeException e) {
			assertTrue(true);
		}
	}

	/**
	 * Testing method to test if the refund calculating method throws
	 * {@link RuntimeException} when sending null data.
	 */
	@Test
	public void testCalculateRefundNullData()
	{
		try {
			m_cancelReservation.calculateRefund(null);
			/* If the method returns here then no exception was thrown. */
			fail("The reservation with null data doesn't throw exception as axpected.");
		}
		catch (RuntimeException e) {
			assertTrue(true);
		}
	}
}
