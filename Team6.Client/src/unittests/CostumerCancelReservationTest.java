
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
import common.ITimeProvider;
import common.TimeProviderStub;
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
import messages.IMessagesFactory;
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

	private ClientMock m_clientMock;

	private CostumerCancelReservation m_cancelReservation;

	private List<Reservation> m_reservationList;

	private Costumer m_costumer;

	/**
	 * Set up the testing class.
	 *
	 * @throws Exception
	 *             An exception on setup failure.
	 */
	@Before
	public void setUp() throws Exception
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, 2, 7, 11, 0, 0);

		IMessagesFactory messageFactoryStub = new MessageFactoryStub();
		ITimeProvider timeProviderStub = new TimeProviderStub(calendar.getTime());

		m_reservationList = new ArrayList<>();

		// Set reservations with legitimate times.
		calendar.add(Calendar.MINUTE, 30);
		m_reservationList.add(generateReservation(1, calendar.getTime()));
		calendar.add(Calendar.MINUTE, 60);
		m_reservationList.add(generateReservation(2, calendar.getTime()));
		calendar.add(Calendar.MINUTE, 60);
		m_reservationList.add(generateReservation(3, calendar.getTime()));
		calendar.add(Calendar.MINUTE, 60);
		m_reservationList.add(generateReservation(4, calendar.getTime()));

		// set reservations with Boundary values
		calendar.set(2018, 2, 7, 11, 59, 0);
		m_reservationList.add(generateReservation(5, calendar.getTime()));
		calendar.set(2018, 2, 7, 12, 1, 0);
		m_reservationList.add(generateReservation(6, calendar.getTime()));
		calendar.set(2018, 2, 7, 13, 59, 0);
		m_reservationList.add(generateReservation(7, calendar.getTime()));
		calendar.set(2018, 2, 7, 14, 1, 0);
		m_reservationList.add(generateReservation(8, calendar.getTime()));
		calendar.set(2018, 2, 7, 10, 59, 0);
		m_reservationList.add(generateReservation(9, calendar.getTime()));

		m_costumer = generateCostumer(1);
		m_clientMock = new ClientMock(m_reservationList, m_costumer);
		m_cancelReservation = new CostumerCancelReservation(m_clientMock, messageFactoryStub, timeProviderStub);

	}

	/**
	 * The method create {@link Reservation} instance with relevant fields for
	 * testing.
	 */
	private Reservation generateReservation(int reservationId, Date deliveryDate)
	{
		Reservation reservation = new Reservation();
		reservation.setId(reservationId);
		reservation.setBlessingCard("Happy testing to everyone.");
		reservation.setCostumerId(1);
		reservation.setCreditCard("1234123412341234");
		reservation.setDeliveryAddress("Ort Braude");
		reservation.setDeliveryDate(deliveryDate);
		reservation.setDeliveryName("Tester");
		reservation.setDeliveryPhone("0542123123");
		reservation.setDeliveryType(ReservationDeliveryType.Future);
		reservation.setNumberOfItems(5);
		reservation.setPrice(100);
		reservation.setShopManagerId(1);
		reservation.setType(ReservationType.Open);
		return reservation;
	}

	/**
	 * The method create {@link Costumer} instance with relevant fields for testing.
	 */
	private Costumer generateCostumer(int costumerId)
	{
		Costumer costumer = new Costumer();
		costumer.setId(costumerId);
		costumer.setBalance(0);
		costumer.setEmail("");
		costumer.setPassword("123");
		costumer.setPrivilege(UserPrivilege.Costumer);
		costumer.setStatus(UserStatus.Connected);
		costumer.setUserName("costumer");
		return costumer;
	}

	/**
	 * Testing method that test the canceling method when the time is set to less
	 * than a hour before the delivery time.
	 */
	@Test
	public void testCancelReservationOneHourBefore()
	{
		m_clientMock.setMessagesHandler(new SucceedScenarioMessageHandler(0));

		cancelReservations(0, 4, 8);
	}

	/**
	 * Testing method that test the canceling method when the time is set to less
	 * than three hours before the delivery time and more than one hour.
	 */
	@Test
	public void testCancelReservationBetweenOneAndThreeHoursBefore()
	{
		m_clientMock.setMessagesHandler(new SucceedScenarioMessageHandler(50));

		cancelReservations(1, 2, 5, 6);
	}

	/**
	 * Testing method that test the canceling method when the time is set to more
	 * than three hours before the delivery time.
	 */
	@Test
	public void testCancelReservationMoreThanThreeHoursBefore()
	{
		m_clientMock.setMessagesHandler(new SucceedScenarioMessageHandler(100));

		cancelReservations(3, 7);
	}

	/**
	 * Method that sends several reservations to the canceling method and expects
	 * true return value.
	 *
	 * @param reservationsId
	 *            Reservation ID (possible several ID numbers) that shall be
	 *            canceled.
	 */
	private void cancelReservations(int... reservationsId)
	{
		for (int i = 0; i < reservationsId.length; i++) {
			int id = reservationsId[i];
			Reservation firstReservation = m_reservationList.get(id);
			assertTrue("Failed to send reservation cancel. Reservation: " + firstReservation,
					m_cancelReservation.cancelReservation(m_costumer, firstReservation));
		}
	}

	/**
	 * Testing method to test if a reservation with wrong reservation ID is
	 * delivered to the server and returns false for the update request.
	 */
	@Test
	public void testCancelReservationIdNotExist()
	{
		m_clientMock
				.setMessagesHandler(new FailureScenarioMessageHandler("The reservation with wrong ID was accepted."));

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
		m_clientMock.setMessagesHandler(
				new FailureScenarioMessageHandler("The reservation with wrong costumer ID was accepted."));

		/* Create reservation instance with not existing costumer ID */
		Reservation reservation = new Reservation(m_reservationList.get(2));
		reservation.setCostumerId(2);
		assertTrue("Failed to send reservation cancel. Reservation: " + reservation,
				m_cancelReservation.cancelReservation(m_costumer, reservation));
	}

	/**
	 * Testing method to test if a costumer with wrong ID is delivered to the server
	 * and returns false for the update request.
	 */
	@Test
	public void testCancelReservationCostumerNotExist()
	{
		m_clientMock.setMessagesHandler((msg) -> {
			/* Parse the response from the mock. */
			IMessageData messageData = msg.getMessageData();
			RespondMessageData respondMessageData = (RespondMessageData) messageData;
			IEntity entity = ((EntityData) respondMessageData.getMessageData()).getEntity();
			if (entity instanceof Costumer) {
				assertFalse("The costumer with wrong ID was accepted.", respondMessageData.isSucceed());
			}
		});
		/* Create costumer instance with not existing ID */
		Costumer costumer = new Costumer(m_costumer);
		costumer.setId(2);
		assertTrue("Failed to send reservation cancel. Costumer: " + costumer,
				m_cancelReservation.cancelReservation(costumer, m_reservationList.get(3)));
	}

	/**
	 * Testing method to test if a reservation with wrong shop ID is delivered to
	 * the server and returns false for the update request.
	 */
	@Test
	public void testCancelReservationShopIdNotExist()
	{
		m_clientMock.setMessagesHandler(
				new FailureScenarioMessageHandler("The reservation with wrong shop ID was accepted."));

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

	/**
	 * SucceedScenarioMessageHandler: Message handler class for succeed scenario
	 * tests.
	 */
	private class SucceedScenarioMessageHandler implements IMessageReceiveHandler
	{

		private float m_expectedBalance;

		public SucceedScenarioMessageHandler(float expectedBalance)
		{
			m_expectedBalance = expectedBalance;
		}

		@Override
		public void onMessageReceived(Message msg) throws Exception
		{
			IMessageData messageData = msg.getMessageData();
			RespondMessageData respondMessageData = (RespondMessageData) messageData;
			IEntity entity = ((EntityData) respondMessageData.getMessageData()).getEntity();
			if (entity instanceof Costumer) {
				assertTrue("The costumer balance was not updated.", respondMessageData.isSucceed());
				Costumer costumer = (Costumer) entity;
				assertTrue(
						"The costumer balance calculation is not correct. Expected balanace: " + m_expectedBalance
								+ ", received balance: " + costumer.getBalance(),
						costumer.getBalance() == m_expectedBalance);
				costumer.setBalance(0);
			} else if (entity instanceof Reservation) {
				Reservation reservation = (Reservation) entity;
				assertTrue("The reservation was not updated.",
						respondMessageData.isSucceed() && reservation.getType() == ReservationType.Canceled);
			}
		}
	}

	/**
	 * FailureScenarioMessageHandler: Message handler class for failure scenario
	 * tests.
	 */
	private class FailureScenarioMessageHandler implements IMessageReceiveHandler
	{

		private String m_stringMessage;

		public FailureScenarioMessageHandler(String msg)
		{
			m_stringMessage = msg;
		}

		@Override
		public void onMessageReceived(Message msg) throws Exception
		{
			IMessageData messageData = msg.getMessageData();
			RespondMessageData respondMessageData = (RespondMessageData) messageData;
			IEntity entity = ((EntityData) respondMessageData.getMessageData()).getEntity();
			if (entity instanceof Reservation) {
				assertFalse(m_stringMessage, respondMessageData.isSucceed());
			}
		}
	}
}
