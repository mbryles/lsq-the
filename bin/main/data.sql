create schema if not exists LSQ;

DROP TABLE IF EXISTS LSQ.SUPPLIER;

CREATE TABLE LSQ.SUPPLIER (

                               SUPPLIER_ID VARCHAR(250),
                               INVOICE_ID VARCHAR(250),
                               INVOICE_DATE DATE,
                               INVOICE_AMOUNT FLOAT,
                               TERMS INTEGER,
                               PAYMENT_DATE DATE,
                               PAYMENT_AMOUNT FLOAT,
                               INVOICE_STATE VARCHAR(100),
                               PRIMARY KEY (SUPPLIER_ID, INVOICE_ID)

);

INSERT INTO LSQ.SUPPLIER ( SUPPLIER_ID,
                           INVOICE_ID,
                           INVOICE_DATE,
                           INVOICE_AMOUNT,
                           TERMS,
                           PAYMENT_DATE,
                           PAYMENT_AMOUNT,
                           INVOICE_STATE
)
VALUES
(
    'ACME',
    'ER12366',
    PARSEDATETIME('26 Aug 2020','dd MMM yyyy'),
    4200.93,
    90,
    PARSEDATETIME('3 Sep 2020','dd MMM yyyy'),
    450.00,
    'OPEN'
),
(
    'ACME',
    'ER12367',
    PARSEDATETIME('23 Sep 2020','dd MMM yyyy'),
    3750.93,
    90,
    PARSEDATETIME('10 Oct 2020','dd MMM yyyy'),
    0.0,
    'LATE'
),
(
    'ACME',
    'ER12368',
    PARSEDATETIME('19 Oct 2020','dd MMM yyyy'),
    3750.93,
    90,
    PARSEDATETIME('7 Nov 2020','dd MMM yyyy'),
    1000.0,
    'PAYMENT_SCHEDULED'
),
(
    'ACME',
    'ER12369',
    PARSEDATETIME('23 Sep 2020','dd MMM yyyy'),
    3750.93,
    90,
    PARSEDATETIME('10 Oct 2020','dd MMM yyyy'),
    0.0,
    'LATE'
),
(
    'ACME',
    'ER12370',
    PARSEDATETIME('1 Jun 2020','dd MMM yyyy'),
    1950.00,
    90,
    PARSEDATETIME('17 Jul 2020','dd MMM yyyy'),
    1950.0,
    'CLOSED'
),
(
    'MEEPO',
    'MT0092',
    PARSEDATETIME('13 Apr 2020','dd MMM yyyy'),
    830.30,
    120,
    PARSEDATETIME('10 Jun 2020','dd MMM yyyy'),
    0.0,
    'LATE'
),
(
    'MEEPO',
    'MT0093',
    PARSEDATETIME('12 Jan 2020','dd MMM yyyy'),
    830.30,
    120,
    PARSEDATETIME('17 Mar 2020','dd MMM yyyy'),
    250.0,
    'OPEN'
);

