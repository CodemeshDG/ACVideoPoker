package com.dommyg.acvideopoker;

class Card {
    enum Suit {
        SPADE (1, R.string.suit_spade),
        CLUB (2, R.string.suit_club),
        HEART (3, R.string.suit_heart),
        DIAMOND (4, R.string.suit_diamond);

        private final int numValue;
        private final int stringValue;

        Suit(int numValue, int stringValue) {
            this.numValue = numValue;
            this.stringValue = stringValue;
        }

        public int getNumValue() {
            return numValue;
        }

        public int getStringValue() {
            return stringValue;
        }
    }

    enum Value {
        TWO (2, R.string.value_two),
        THREE (3, R.string.value_three),
        FOUR (4, R.string.value_four),
        FIVE (5, R.string.value_five),
        SIX (6, R.string.value_six),
        SEVEN (7, R.string.value_seven),
        EIGHT (8, R.string.value_eight),
        NINE (9, R.string.value_nine),
        TEN (10, R.string.value_ten),
        JACK (11, R.string.value_jack),
        QUEEN (12, R.string.value_queen),
        KING (13, R.string.value_king),
        ACE (14, R.string.value_ace);

        private final int numValue;
        private final int stringValue;

        Value(int numValue, int stringValue) {
            this.numValue = numValue;
            this.stringValue = stringValue;
        }

        public int getNumValue() {
            return numValue;
        }

        public int getStringValue() {
            return stringValue;
        }
    }

    private Suit suit;
    private Value value;

    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }
}
