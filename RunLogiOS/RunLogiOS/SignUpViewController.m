//
//  SignUpViewController.m
//  RunLogiOS
//
//  Created by Ariana Antonio on 11/2/14.
//  Copyright (c) 2014 Ariana Antonio. All rights reserved.
//

#import "SignUpViewController.h"
#import <Parse/Parse.h>

@interface SignUpViewController ()

@end

@implementation SignUpViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [_usernameField setDelegate:self];
    [_passwordField setDelegate:self];
    // Do any additional setup after loading the view.
    
    reachGoogle = [Reachability reachabilityWithHostName:@"www.google.com"];
    checkNetworkStatus = [reachGoogle currentReachabilityStatus];
    
    if (checkNetworkStatus == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Please connect to a network" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    }
}
-(void)signUp:(id)sender {
    
    checkNetworkStatus = [reachGoogle currentReachabilityStatus];
    
    if (checkNetworkStatus == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Please connect to a network" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    } else {
        PFUser *user = [PFUser user];
        user.username = [_usernameField text];
        user.password = [_passwordField text];
        
        [user signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
            if (!error) {
                // Hooray! Let them use the app now.
                [self performSegueWithIdentifier:@"signUpToAddRunSegue" sender:self];
            } else {
                NSString *errorString = [error userInfo][@"error"];
                NSLog(@"Error: %@", errorString);
                if ([error code] == 202) {
                    UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Username already in use" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
                    [alert show];
                }
                // Show the errorString somewhere and let the user try again.
            }
        }];
    }
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    
    [_usernameField resignFirstResponder];
    [_passwordField resignFirstResponder];
    return YES;
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
