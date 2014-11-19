//
//  AddRunViewController.h
//  RunLogiOS
//
//  Created by Ariana Antonio on 11/2/14.
//  Copyright (c) 2014 Ariana Antonio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Reachability.h"

@interface AddRunViewController : UIViewController
{
    NSMutableArray *runArray;
    NSMutableDictionary *runDictionary;
    NSString *userId;
    Reachability *reachGoogle;
    NetworkStatus checkNetworkStatus;
}
@property (nonatomic, strong) IBOutlet UITextField *distanceField;
@property (nonatomic, strong) IBOutlet UIDatePicker *runDate;
@property (nonatomic, strong) IBOutlet UITableView *runTable;
@property (nonatomic, strong) IBOutlet UILabel *usernameLabel;


-(IBAction)addRun:(id)sender;
-(IBAction)deleteRun:(id)sender;
-(IBAction)signOut:(id)sender;
-(IBAction)refresh:(id)sender;

@end
