package com.core.orderhub.backend.domain.enums;

public enum OrderStatus {

    CREATED {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return newStatus == PAID || newStatus == CANCELED;
        }
    },

    PAID {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return newStatus == FINISHED || newStatus == CANCELED;
        }
    },

    FINISHED {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return false;
        }
    },

    CANCELED {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(OrderStatus newStatus);
}