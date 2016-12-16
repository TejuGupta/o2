// coercetest.c -- test coercion of O2 parameters
//
// send from all coercible types: i, h, f, d, t to 
// handlers that ask for i, h, f, d, t, B, T, F

#include <stdio.h>
#include "o2.h"
#include "assert.h"
#include "string.h"


int got_the_message = FALSE;

// we do not declare a different handler for each send type, but
// we check that the message has the expected type string. To
// enable the test, we put the sender's type string in this
// global:
//
char *send_types = "";

// receive float as int
int service_i(const o2_message_ptr data, const char *types,
              o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    assert(strcmp(types, send_types) == 0);
    o2_arg_ptr arg = o2_get_next('i');
    assert(arg);
    printf("service_i types=%s int=%d\n", types, arg->i);
    assert(arg->i == 12345);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


int service_ip(const o2_message_ptr data, const char *types,
               o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(strcmp(types, "i") == 0);
    assert(argc == 1);
    assert(argv[0]);
    printf("service_ip types=%s int=%d\n", types, argv[0]->i);
    assert(argv[0]->i == ((strcmp(send_types, "T") == 0 ||
                           strcmp(send_types, "B") == 0) ? 1 :
                          ((strcmp(send_types, "F") == 0) ? 0 : 12345)));
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive int as bool
int service_B(const o2_message_ptr data, const char *types,
              o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    assert(strcmp(types, send_types) == 0);
    o2_arg_ptr arg = o2_get_next('B');
    assert(arg);
    printf("service_B types=%s bool=%d\n", types, arg->B);
    assert(arg->B == TRUE);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


int service_Bp(const o2_message_ptr data, const char *types,
               o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(strcmp(types, "B") == 0);
    assert(argc == 1);
    assert(argv[0]);
    printf("service_Bp types=%s bool=%d\n", types, argv[0]->B);
    assert(argv[0]->B == (strcmp(send_types, "F") != 0));
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive int32 as int64
int service_h(const o2_message_ptr data, const char *types,
              o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    assert(strcmp(types, send_types) == 0);
    o2_arg_ptr arg = o2_get_next('h');
    assert(arg);
    printf("service_h types=%s int64=%lld\n", types, arg->h);
    assert(arg->h == 12345LL);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


int service_hp(const o2_message_ptr data, const char *types,
               o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(strcmp(types, "h") == 0);
    assert(argc == 1);
    assert(argv[0]);
    printf("service_hp types=%s int64=%lld\n", types, argv[0]->h);
    assert(argv[0]->h == ((strcmp(send_types, "T") == 0 ||
                           strcmp(send_types, "B") == 0) ? 1 :
                          ((strcmp(send_types, "F") == 0) ? 0 : 12345)));
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive int32 as float
int service_f(const o2_message_ptr data, const char *types,
              o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    assert(strcmp(types, send_types) == 0);
    o2_arg_ptr arg = o2_get_next('f');
    assert(arg);
    printf("service_f types=%s float=%g\n", types, arg->f);
    assert(arg->f == 1234.0);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


int service_fp(const o2_message_ptr data, const char *types,
               o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(strcmp(types, "f") == 0);
    assert(argc == 1);
    assert(argv[0]);
    printf("service_fp types=%s float=%g\n", types, argv[0]->f);
    assert(argv[0]->f == ((strcmp(send_types, "T") == 0 ||
                           strcmp(send_types, "B") == 0) ? 1 :
                          ((strcmp(send_types, "F") == 0) ? 0 : 1234.0)));
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive int32 as double
int service_d(const o2_message_ptr data, const char *types,
              o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    assert(strcmp(types, send_types) == 0);
    o2_arg_ptr arg = o2_get_next('d');
    assert(arg);
    printf("service_d types=%s double=%g\n", types, arg->d);
    assert(arg->d == 1234.0);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


int service_dp(const o2_message_ptr data, const char *types,
               o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(strcmp(types, "d") == 0);
    assert(argc == 1);
    assert(argv[0]);
    printf("service_dp types=%s double=%g\n", types, argv[0]->d);
    assert(argv[0]->d == ((strcmp(send_types, "T") == 0 ||
                           strcmp(send_types, "B") == 0) ? 1 :
                          ((strcmp(send_types, "F") == 0) ? 0 : 1234.0)));
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive int32 as time
int service_t(const o2_message_ptr data, const char *types,
              o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    assert(strcmp(types, send_types) == 0);
    o2_arg_ptr arg = o2_get_next('t');
    assert(arg);
    printf("service_t types=%s time=%g\n", types, arg->t);
    assert(arg->t == 1234.0);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive int32 as time
int service_tp(const o2_message_ptr data, const char *types,
               o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(strcmp(types, "t") == 0);
    assert(argc == 1);
    assert(argv[0]);
    printf("service_tp types=%s time=%g\n", types, argv[0]->t);
    assert(argv[0]->t == 1234.0);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive a string as a Symbol
int service_S(const o2_message_ptr data, const char *types,
              o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    assert(strcmp(types, send_types) == 0);
    o2_arg_ptr arg = o2_get_next('S');
    assert(arg);
    printf("service_S types=%s symbol=%s\n", types, arg->S);
    assert(strcmp(arg->S, "aString") == 0);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive string as "S" coerced into argv
int service_Sp(const o2_message_ptr data, const char *types,
               o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(strcmp(types, "S") == 0);
    assert(argc == 1);
    assert(argv[0]);
    printf("service_Sp types=%s symbol=%s\n", types, argv[0]->S);
    assert(strcmp(argv[0]->S, "aString") == 0);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive sent Symbol as s
int service_s(const o2_message_ptr data, const char *types,
              o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    assert(strcmp(types, send_types) == 0);
    o2_arg_ptr arg = o2_get_next('s');
    assert(arg);
    printf("service_S types=%s symbol=%s\n", types, arg->s);
    assert(strcmp(arg->s, "aSymbol") == 0);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive Symbol coerced to "s" string in argv
int service_sp(const o2_message_ptr data, const char *types,
               o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(strcmp(types, "s") == 0);
    assert(argc == 1);
    assert(argv[0]);
    printf("service_Sp types=%s string=%s\n", types, argv[0]->s);
    assert(strcmp(argv[0]->s, "aSymbol") == 0);
    got_the_message = TRUE;
    return O2_SUCCESS;
}



// receive different sent types as True
int service_T(const o2_message_ptr data, const char *types,
              o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    assert(strcmp(types, send_types) == 0);
    o2_arg_ptr arg = o2_get_next('T');
    printf("service_T types=%s\n", types);
    assert(arg);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


int service_Tp(const o2_message_ptr data, const char *types,
               o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(strcmp(types, "T") == 0);
    assert(argc == 1);
    printf("service_Tp types=%s\n", types);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


// receive int32 as FALSE
int service_F(const o2_message_ptr data, const char *types,
              o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    assert(strcmp(types, send_types) == 0);
    o2_arg_ptr arg = o2_get_next('F');
    assert(arg);
    printf("service_F types=%s\n", types);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


int service_Fp(const o2_message_ptr data, const char *types,
               o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(strcmp(types, "F") == 0);
    assert(argc == 1);
    printf("service_Fp types=%s\n", types);
    got_the_message = TRUE;
    return O2_SUCCESS;
}




// expects hifdt, but receives them as ihdff
//
int service_many(const o2_message_ptr data, const char *types,
    o2_arg_ptr *argv, int argc, void *user_data)
{
    o2_start_extract(data);
    o2_arg_ptr arg = o2_get_next('i');
    assert(arg->i == 12345);
    arg = o2_get_next('h');
    assert(arg->h == 1234LL);
    arg = o2_get_next('d');
    // note that we must convert the double back to a float
    // and compare to a float, because if you assign 123.456
    // to a float, the stored value is approximately 123.45600128173828
    assert(((float) arg->d) == 123.456F);
    arg = o2_get_next('f');
    assert(arg->f == 123.456F);
    arg = o2_get_next('f');
    assert(arg->f == 123.456F);
    assert(strcmp(types, "hifdt") == 0);
    printf("service_many types=%s\n", types);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


int service_manyp(const o2_message_ptr data, const char *types,
                  o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(argc == 5);
    assert(argv[0]->i == 12345);
    assert(argv[1]->h == 1234LL);
    // note that we must convert the double back to a float
    // and compare to a float, because if you assign 123.456
    // to a float, the stored value is approximately 123.45600128173828
    assert(((float) argv[2]->d) == 123.456F);
    assert(argv[3]->f == 123.456F);
    assert(argv[4]->f == 123.456F);
    assert(strcmp(types, "ihdff") == 0);
    printf("service_manyp types=%s\n", types);
    got_the_message = TRUE;
    return O2_SUCCESS;
}


void send_the_message()
{
    while (!got_the_message) {
        o2_poll();
    }
    got_the_message = FALSE;
}


int main(int argc, const char * argv[])
{
    o2_initialize("test");    
    o2_add_service("one");

    char address[32];
    for (int i = 0; i < 5; i++) {
        char send_type = ("ihfdt")[i];
        char send_types[4];
        send_types[0] = send_type;
        send_types[1] = 0;
        snprintf(address, 32, "/one/%ci", send_type);
        o2_add_method(address, send_types, &service_i, NULL, TRUE, FALSE);

        snprintf(address, 32, "/one/%cB", send_type);
        o2_add_method(address, send_types, service_B, NULL, TRUE, FALSE);
    
        snprintf(address, 32, "/one/%ch", send_type);
        o2_add_method(address, send_types, service_h, NULL, TRUE, FALSE);

        snprintf(address, 32, "/one/%cf", send_type);
        o2_add_method(address, send_types, service_f, NULL, TRUE, FALSE);

        snprintf(address, 32, "/one/%cd", send_type);
        o2_add_method(address, send_types, service_d, NULL, TRUE, FALSE);

        snprintf(address, 32, "/one/%ct", send_type);
        o2_add_method(address, send_types, service_t, NULL, TRUE, FALSE);

        snprintf(address, 32, "/one/%cT", send_type);
        o2_add_method(address, send_types, service_T, NULL, TRUE, FALSE);

        snprintf(address, 32, "/one/%cF", send_type);
        o2_add_method(address, send_types, service_F, NULL, TRUE, FALSE);
    }
    o2_add_method("/one/sS", "S", service_S, NULL, TRUE, FALSE);
    o2_add_method("/one/Ss", "s", service_s, NULL, TRUE, FALSE);
    
    o2_add_method("/one/ip", "i", &service_ip, NULL, TRUE, TRUE);
    o2_add_method("/one/Bp", "B", &service_Bp, NULL, TRUE, TRUE);
    o2_add_method("/one/hp", "h", &service_hp, NULL, TRUE, TRUE);
    o2_add_method("/one/fp", "f", &service_fp, NULL, TRUE, TRUE);
    o2_add_method("/one/dp", "d", &service_dp, NULL, TRUE, TRUE);
    o2_add_method("/one/tp", "t", &service_tp, NULL, TRUE, TRUE);
    o2_add_method("/one/Tp", "T", &service_Tp, NULL, TRUE, TRUE);
    o2_add_method("/one/Fp", "F", &service_Fp, NULL, TRUE, TRUE);
    o2_add_method("/one/sp", "s", &service_sp, NULL, TRUE, TRUE);
    o2_add_method("/one/Sp", "S", &service_Sp, NULL, TRUE, TRUE);

    o2_add_method("/one/many", "hifdt", &service_many, NULL, TRUE, FALSE);
    o2_add_method("/one/manyp", "ihdff", &service_manyp, NULL, TRUE, TRUE);

    o2_send("/one/many", 0, "hifdt", 12345LL, 1234,
            123.456, 123.456, 123.456);
    o2_send("/one/manyp", 0, "hifdt", 12345LL, 1234,
            123.456, 123.456, 123.456);

    send_types = "i";
    o2_send("/one/ii", 0, "i", 12345);
    o2_send("/one/ip", 0, "i", 12345);
    send_the_message();
    o2_send("/one/iB", 0, "i", 1234);
    o2_send("/one/Bp", 0, "i", 1234);
    send_the_message();
    o2_send("/one/ih", 0, "i", 12345);
    o2_send("/one/hp", 0, "i", 12345);
    send_the_message();
    o2_send("/one/if", 0, "i", 1234);
    o2_send("/one/fp", 0, "i", 1234);
    send_the_message();
    o2_send("/one/id", 0, "i", 1234);
    o2_send("/one/dp", 0, "i", 1234);
    send_the_message();
    o2_send("/one/it", 0, "i", 1234);
    o2_send("/one/tp", 0, "i", 1234);
    send_the_message();
    o2_send("/one/iT", 0, "i", 1111);
    o2_send("/one/Tp", 0, "i", 1111);
    send_the_message();
    o2_send("/one/iF", 0, "i", 0);
    o2_send("/one/Fp", 0, "i", 0);
    send_the_message();

    send_types = "h";
    o2_send("/one/hi", 0, "h", 12345LL);
    o2_send("/one/ip", 0, "h", 12345LL);
    send_the_message();
    o2_send("/one/hB", 0, "h", 12345LL);
    o2_send("/one/Bp", 0, "h", 12345LL);
    send_the_message();
    o2_send("/one/hh", 0, "h", 12345LL);
    o2_send("/one/hp", 0, "h", 12345LL);
    send_the_message();
    o2_send("/one/hf", 0, "h", 1234LL);
    o2_send("/one/fp", 0, "h", 1234LL);
    send_the_message();
    o2_send("/one/hd", 0, "h", 1234LL);
    o2_send("/one/dp", 0, "h", 1234LL);
    send_the_message();
    o2_send("/one/ht", 0, "h", 1234LL);
    o2_send("/one/tp", 0, "h", 1234LL);
    send_the_message();
    o2_send("/one/hT", 0, "h", 1111LL);
    o2_send("/one/Tp", 0, "h", 1111LL);
    send_the_message();
    o2_send("/one/hF", 0, "h", 0LL);
    o2_send("/one/Fp", 0, "h", 0LL);
    send_the_message();

    send_types = "f";
    o2_send("/one/fi", 0, "f", 12345.0);
    o2_send("/one/ip", 0, "f", 12345.0);
    send_the_message();
    o2_send("/one/fB", 0, "f", 1234.0);
    o2_send("/one/Bp", 0, "f", 1234.0);
    send_the_message();
    o2_send("/one/fh", 0, "f", 12345.0);
    o2_send("/one/hp", 0, "f", 12345.0);
    send_the_message();
    o2_send("/one/ff", 0, "f", 1234.0);
    o2_send("/one/fp", 0, "f", 1234.0);
    send_the_message();
    o2_send("/one/fd", 0, "f", 1234.0);
    o2_send("/one/dp", 0, "f", 1234.0);
    send_the_message();
    o2_send("/one/ft", 0, "f", 1234.0);
    o2_send("/one/tp", 0, "f", 1234.0);
    send_the_message();
    o2_send("/one/fT", 0, "f", 1111.0);
    o2_send("/one/Tp", 0, "f", 1111.0);
    send_the_message();
    o2_send("/one/fF", 0, "f", 0.0);
    o2_send("/one/Fp", 0, "f", 0.0);
    send_the_message();

    send_types = "d";
    o2_send("/one/di", 0, "d", 12345.0);
    o2_send("/one/ip", 0, "d", 12345.0);
    send_the_message();
    o2_send("/one/dB", 0, "d", 1234.0);
    o2_send("/one/Bp", 0, "d", 1234.0);
    send_the_message();
    o2_send("/one/dh", 0, "d", 12345.0);
    o2_send("/one/hp", 0, "d", 12345.0);
    send_the_message();
    o2_send("/one/df", 0, "d", 1234.0);
    o2_send("/one/fp", 0, "d", 1234.0);
    send_the_message();
    o2_send("/one/dd", 0, "d", 1234.0);
    o2_send("/one/dp", 0, "d", 1234.0);
    send_the_message();
    o2_send("/one/dt", 0, "d", 1234.0);
    o2_send("/one/tp", 0, "d", 1234.0);
    send_the_message();
    o2_send("/one/dT", 0, "d", 1111.0);
    o2_send("/one/Tp", 0, "d", 1111.0);
    send_the_message();
    o2_send("/one/dF", 0, "d", 0.0);
    o2_send("/one/Fp", 0, "d", 0.0);
    send_the_message();

    send_types = "t";
    o2_send("/one/ti", 0, "t", 12345.0);
    o2_send("/one/ip", 0, "t", 12345.0);
    send_the_message();
    o2_send("/one/th", 0, "t", 12345.0);
    o2_send("/one/hp", 0, "t", 12345.0);
    send_the_message();
    o2_send("/one/tf", 0, "t", 1234.0);
    o2_send("/one/fp", 0, "t", 1234.0);
    send_the_message();
    o2_send("/one/td", 0, "t", 1234.0);
    o2_send("/one/dp", 0, "t", 1234.0);
    send_the_message();
    o2_send("/one/tt", 0, "t", 1234.0);
    o2_send("/one/tp", 0, "t", 1234.0);
    send_the_message();


    send_types = "T";
    o2_send("/one/Ti", 0, "T");
    o2_send("/one/ip", 0, "T");
    send_the_message();
    o2_send("/one/TB", 0, "T");
    o2_send("/one/Bp", 0, "T");
    send_the_message();
    o2_send("/one/Th", 0, "T");
    o2_send("/one/hp", 0, "T");
    send_the_message();
    o2_send("/one/Tf", 0, "T");
    o2_send("/one/fp", 0, "T");
    send_the_message();
    o2_send("/one/Td", 0, "T");
    o2_send("/one/dp", 0, "T");
    send_the_message();
    o2_send("/one/TT", 0, "T");
    o2_send("/one/Tp", 0, "T");
    send_the_message();

    send_types = "F";
    o2_send("/one/Fi", 0, "F");
    o2_send("/one/ip", 0, "F");
    send_the_message();
    o2_send("/one/FB", 0, "F");
    o2_send("/one/Bp", 0, "F");
    send_the_message();
    o2_send("/one/Fh", 0, "F");
    o2_send("/one/hp", 0, "F");
    send_the_message();
    o2_send("/one/Ff", 0, "F");
    o2_send("/one/fp", 0, "F");
    send_the_message();
    o2_send("/one/Fd", 0, "F");
    o2_send("/one/dp", 0, "F");
    send_the_message();
    o2_send("/one/FF", 0, "F");
    o2_send("/one/Fp", 0, "F");
    send_the_message();

    send_types = "B";
    o2_send("/one/Bi", 0, "B", TRUE);
    o2_send("/one/ip", 0, "B", TRUE);
    send_the_message();
    o2_send("/one/BB", 0, "B", TRUE);
    o2_send("/one/Bp", 0, "B", TRUE);
    send_the_message();
    o2_send("/one/Bh", 0, "B", TRUE);
    o2_send("/one/hp", 0, "B", TRUE);
    send_the_message();
    o2_send("/one/Bf", 0, "B", TRUE);
    o2_send("/one/fp", 0, "B", TRUE);
    send_the_message();
    o2_send("/one/Bd", 0, "B", TRUE);
    o2_send("/one/dp", 0, "B", TRUE);
    send_the_message();
    o2_send("/one/BB", 0, "B", TRUE);
    o2_send("/one/Bp", 0, "B", TRUE);
    send_the_message();
    o2_send("/one/BF", 0, "B", FALSE);
    o2_send("/one/Fp", 0, "B", FALSE);
    send_the_message();
    o2_send("/one/BT", 0, "B", TRUE);
    o2_send("/one/Tp", 0, "B", TRUE);
    send_the_message();
    
    send_types = "S";
    o2_send("/one/Ss", 0, "S", "aSymbol");
    o2_send("/one/sp", 0, "S", "aSymbol");
    
    send_types = "s";
    o2_send("/one/sS", 0, "s", "aString");
    o2_send("/one/Sp", 0, "s", "aString");

    printf("DONE\n");
    o2_finish();
    return 0;
}