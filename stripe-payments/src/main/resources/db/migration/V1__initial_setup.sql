-- [me] Not used
create table PAYMENTS  (
    id int auto_increment primary key,
    -- [N] For idempotency: multiple webhooks pmt events could occur for the same event.    
    payment_intent_id varchar(255) unique,
    email varchar(255),
    amount int,
    feature_request varchar(200)
);