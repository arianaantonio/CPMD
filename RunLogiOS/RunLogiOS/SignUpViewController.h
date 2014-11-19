//
//  SignUpViewController.h
//  RunLogiOS
//
//  Created by Ariana Antonio on 11/2/14.
//  Copyright (c) 2014 Ariana Antonio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Reachability.h"

@interface SignUpViewController : UIViewController
{
    NSString *username;
    NSString *password;
    Reachability *reachGoogle;
    NetworkStatus checkNetworkStatus;
}

@property (nonatomic, strong) IBOutlet UITextField *usernameField;
@property (nonatomic, strong) IBOutlet UITextField *passwordField;

-(IBAction)signUp:(id)sender;

@end
