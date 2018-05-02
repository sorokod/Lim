# Lim

Lim is a small Java library that implements a variant of the [Token_bucket](https://en.wikipedia.org/wiki/Token_bucket).
algorithm. It can be used to enforce HTTP call rate, limit the number of
failed login attempts, implement [Circuit breaker](https://en.wikipedia.org/wiki/Circuit_breaker_design_pattern)
like logic, etc...

## Usage

An instance of `Lim` is configured with a quota of _tokens_ to be made
available over a time window. Given a quota `Q` and a time window of `T`
time units, tokens will be granted if and only if:

> the number of tokens granted within the last `T` time units __plus__ the
  tokens requested, does not exceed `Q`.

For example, with the following definition

```java
Lim lim = Lim.builder()
    .quota(100)
    .timeWindow(2, SECONDS)
    .build();
```

tokens will be granted if and only if the number of tokens granted within
the last two seconds __and__ the tokens requested, does not exceed 100.

Tokens are requested by calling `get( int tokenCount )` which returns
a `boolean` indicating whether the requested number of tokens was granted.

Here is pseudo code that enforces a policy of allowing at most five failed
login attempts within three minutes:

```java
Lim lim = Lim.builder().quota(5).timeWindow(3, MINUTES).build();

...

boolean valid = validate( credentials );
if( valid ) {
    // proceed
} else {
    if ( lim.get( 1 ) ) {
        // redirect to retry
    } else {
        // redirect to "try again latter"
    }
}

```


## Features

* Thread safe
* Small code size, simple logic
* Zero dependencies

## License

Lim is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).