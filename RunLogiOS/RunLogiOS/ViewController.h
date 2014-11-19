//
//  ViewController.h
//  RunLogiOS
//
//  Created by Ariana Antonio on 11/2/14.
//  Copyright (c) 2014 Ariana Antonio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h> 
#import "Reachability.h"

@interface ViewController : UIViewController
{
    NSString *username;
    NSString *password;
    Reachability *reachGoogle;
    NetworkStatus checkNetworkStatus;
}
@property (nonatomic, strong) IBOutlet UITextField *usernameField;
@property (nonatomic, strong) IBOutlet UITextField *passwordField;

-(IBAction)login:(id)sender;
-(IBAction)signUp:(id)sender;

@end

