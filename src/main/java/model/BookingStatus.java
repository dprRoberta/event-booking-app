package model;

import java.io.Serializable;

public enum BookingStatus implements Serializable {
    Active,
    CancelledByConsumer,
    CancelledByProvider,
}
