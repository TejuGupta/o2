//  dispatchtest.c -- dispatch messages between local services
//

#include <stdio.h>
#include "o2.h"
#include "assert.h"
#include "o2_message.h"


#define N_ADDRS 20

int expected = 0;

void service_one(o2_msg_data_ptr data, const char *types,
                 o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(argc == 1);
    assert(argv[0]->i == 1234);
    printf("service_one called\n");
   // assert(expected % 10 == 1);
    //expected /= 10;
}

void service_two(o2_msg_data_ptr data, const char *types,
                 o2_arg_ptr *argv, int argc, void *user_data)
{
    assert(argc == 1);
    assert(argv[0]->i == 2345);
    printf("service_two called\n");
   // assert(expected % 10 == 2);
    //expected /= 10;
}


int main(int argc, const char * argv[])
{
   o2_initialize("test");
   o2_service_new("one");
   o2_method_new("/one/i", "i", &service_one, NULL, TRUE, TRUE);  
   o2_service_new("two");
   o2_method_new("/two/i", "i", &service_two, NULL, TRUE, TRUE);


    // make a bundle, starting with two messages
    o2_send_start();
    o2_add_int32(1234);
   o2_message_ptr one = o2_message_finish(0.0, "/one/i", TRUE);

    o2_send_start();
    o2_add_int32(2345);
    o2_message_ptr two = o2_message_finish(0.0, "/four/three", TRUE); /giving wrong service name path
	    
    o2_send_start();
    o2_add_message(one);
    o2_add_message(two);
    o2_message_ptr bdl = o2_message_finish(0.0, "#one", TRUE);
	

//creating a bundle message and scheduling the msg and giving wrong service name path
	o2_schedule(&o2_ltsched, bdl);

    o2_finish();
    printf("DONE\n");
    return 0;
}

















    /*expected = 21;
    o2_send_start();
//printf("Checking in bundletest if it is coming here 1 \n");
    o2_add_message(one);
//printf("Checking in bundletest if it is coming here 2 \n");
    o2_add_message(two);
   // o2_send_finish(0.0, "#!@#$%", TRUE);
 o2_send_finish(0.0, "#one", TRUE);
//printf("o2_send_finish \n"); 
    assert(expected == 0);

    expected = 21;
    o2_send_start();
    o2_add_message(one);
    o2_add_message(two);
//    int o2_finvar=o2_send_finish(0.0, "#@#$%^", TRUE);
int o2_finvar=o2_send_finish(0.0, "#two/four", TRUE);
printf("o2_finvar %d \n",o2_finvar);
if(o2_finvar==-3)
    assert(expected == 0);
    
    // make a nested bundle ((12)(12))
    o2_send_start();
    o2_add_message(one);
    o2_add_message(two);
    o2_message_ptr bdl = o2_message_finish(0.0, "#one", TRUE);
	//printf("I am in main of bundletest.c \n");

    expected = 2121;
    o2_send_start();
    o2_add_message(bdl);
 //  o2_add_message(bdl);
int checkbundle=o2_add_message(bdl);
//printf("Checking if it is a bundle:%d \n",checkbundle) ;
    o2_send_finish(0.0, "#two", TRUE);
    assert(expected == 0);*/
    


