//
//  ViewController.m
//  RunLogiOS
//
//  Created by Ariana Antonio on 11/2/14.
//  Copyright (c) 2014 Ariana Antonio. All rights reserved.
//

#import "ViewController.h"
#import <Parse/Parse.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [_usernameField setDelegate:self];
    [_passwordField setDelegate:self];
    
    reachGoogle = [Reachability reachabilityWithHostName:@"www.google.com"];
    checkNetworkStatus = [reachGoogle currentReachabilityStatus];
    
    if (checkNetworkStatus == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Please connect to a network" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    }
}
-(void)login:(id)sender {
    
    checkNetworkStatus = [reachGoogle currentReachabilityStatus];
    
    if (checkNetworkStatus == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Please connect to a network" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    } else {
    
        username = [_usernameField text];
        password = [_passwordField text];
        [PFUser logInWithUsernameInBackground:username password:password
                                        block:^(PFUser *user, NSError *error) {
                                            if (user) {
                                                // Do stuff after successful login.
                                                NSLog(@"Logged in");
                                                [self performSegueWithIdentifier:@"loginSegue" sender:self];
                                            } else {
                                                // The login failed. Check error to see why.
                                                NSLog(@"Not logged in");
                                            }
                                        }];
    }
    
}
-(void)signUp:(id)sender {
    
}
-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    
    [_usernameField resignFirstResponder];
    [_passwordField resignFirstResponder];
    return YES;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
