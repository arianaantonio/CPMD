//
//  AddRunViewController.m
//  RunLogiOS
//
//  Created by Ariana Antonio on 11/2/14.
//  Copyright (c) 2014 Ariana Antonio. All rights reserved.
//

#import "AddRunViewController.h"
#import <Parse/Parse.h>


@interface AddRunViewController ()
@property (nonatomic) Reachability *networkReachability;

@end

@implementation AddRunViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    runArray = [[NSMutableArray alloc]init];
    _distanceField.keyboardType = UIKeyboardTypeNumbersAndPunctuation;
    [_distanceField setDelegate:self];
    
    //Reachability *reachGoogle = [Reachability reachabilityWithHostname:@"www.google.com"];
    reachGoogle = [Reachability reachabilityWithHostName:@"www.google.com"];
    checkNetworkStatus = [reachGoogle currentReachabilityStatus];
    
    if (checkNetworkStatus == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Please connect to a network" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    } else {
        
        [NSTimer scheduledTimerWithTimeInterval:20.0
                                         target:self
                                       selector:@selector(syncForUpdates)
                                       userInfo:nil
                                        repeats:YES];
        
        PFUser *currentUser = [PFUser currentUser];
        if (currentUser) {
            userId = currentUser.objectId;
            [self refresh:nil];
        } else {
        }
    }
    
}
-(void)addRun:(id)sender {
    
    NSDate *date = [_runDate date];

    NSString *distance = [_distanceField text];
    NSNumberFormatter *numFormatter = [[NSNumberFormatter alloc] init];
    [numFormatter setNumberStyle:NSNumberFormatterDecimalStyle];
    NSNumber *distanceNum = [numFormatter numberFromString:distance];
    
    NSCharacterSet *stringsAllowed = [[NSCharacterSet characterSetWithCharactersInString:@".0123456789"]invertedSet];
    
    checkNetworkStatus = [reachGoogle currentReachabilityStatus];
    if (checkNetworkStatus == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Please connect to a network" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    } else {
    
        if ([distance rangeOfCharacterFromSet:stringsAllowed].location != NSNotFound) {
            
            UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Invalid character in distance field" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
        } else if ([distance isEqualToString:@""]){
            UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Please enter a distance" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
        } else {
            
            if ([sender tag]== 1) {
                PFObject *newRun = [PFObject objectWithClassName:@"Runs"];
                newRun[@"distance"] = distanceNum;
                newRun[@"date"] = date;
                newRun[@"userID"] = userId;
                [newRun saveInBackground];
                
                NSMutableDictionary *runDictionary2 = [[NSMutableDictionary alloc]init];
                [runDictionary2 setValue:distanceNum forKey:@"distance"];
                [runDictionary2 setValue:date forKey:@"date"];
                [runArray removeAllObjects];
                [_distanceField setText:@""];
                [self refresh:nil];
            } else {
                
                NSIndexPath *selectedIndexPath = [_runTable indexPathForSelectedRow];
                if ([runArray count] != 0) {
                    NSMutableDictionary *runDic = [runArray objectAtIndex:selectedIndexPath.row];
                    NSString *objectID = runDic[@"objectId"];
                    NSLog(@"Object ID: %@", objectID);
                    
                    PFQuery *query = [PFQuery queryWithClassName:@"Runs"];
                    [query getObjectInBackgroundWithId:objectID block:^(PFObject *runUpdate, NSError *error) {

                        runUpdate[@"distance"] = distanceNum;
                        runUpdate[@"date"] = date;
                        [runUpdate saveInBackground];
                        
                        [runArray removeAllObjects];
                        [_distanceField setText:@""];
                        [self refresh:nil];
                    }];
                    [_runTable reloadData];
                }
            }
        }
    }
}
-(void)deleteRun:(id)sender {
    
    checkNetworkStatus = [reachGoogle currentReachabilityStatus];
    if (checkNetworkStatus == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Please connect to a network" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    } else {
    
        NSIndexPath *selectedIndexPath = [_runTable indexPathForSelectedRow];
        if ([runArray count] != 0) {
            NSMutableDictionary *runDic = [runArray objectAtIndex:selectedIndexPath.row];
            NSString *objectID = runDic[@"objectId"];
            NSLog(@"Object ID: %@", objectID);
            
            PFQuery *query = [PFQuery queryWithClassName:@"Runs"];
            [query whereKey:@"objectId" equalTo:objectID];
            
            [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
                if (!error) {
                    
                    for (PFObject *object in objects) {
                        NSLog(@"%@", object.objectId);
                        
                        [object deleteInBackground];
                        
                    }
                } else {
                    NSLog(@"Error: %@ %@", error, [error userInfo]);
                }
            }];
            [runArray removeObjectAtIndex:selectedIndexPath.row];
            [_runTable reloadData];
        }
    }
}
-(IBAction)refresh:(id)sender {
    
    checkNetworkStatus = [reachGoogle currentReachabilityStatus];
    if (checkNetworkStatus == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Please connect to a network" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    } else {
        
        PFQuery *query2 = [PFQuery queryWithClassName:@"Runs"];
        [query2 whereKey:@"userID" equalTo:userId];
        [query2 orderByDescending:@"date"];
        [query2 findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
            
            if (!error) {
                
                NSDate *datePulled;
                NSNumber *distancePulled = 0;
                [runArray removeAllObjects];
                for (PFObject *object in objects) {
                    NSLog(@"%@", object.objectId);
                    distancePulled = [object objectForKey:@"distance"];
                    NSDateFormatter *df = [[NSDateFormatter alloc]init];
                    [df setDateFormat:@"MM/dd/yyyy"];
                    
                    datePulled = [object objectForKey:@"date"];
                    NSString *datePulledString = [df stringFromDate:datePulled];
                    NSLog(@"Distance: %@ , Date: %@", distancePulled, datePulledString);
                    NSMutableDictionary *runDictionary2 = [[NSMutableDictionary alloc]init];
                    
                    [runDictionary2 setValue:distancePulled forKey:@"distance"];
                    [runDictionary2 setValue:datePulledString forKey:@"date"];
                    [runDictionary2 setValue:object.objectId forKey:@"objectId"];
                    
                    [runArray addObject:runDictionary2];
                }
                [_runTable reloadData];
            }
        }];
        [_runTable reloadData];
    }
}
-(void)signOut:(id)sender {
    [PFUser logOut];
    [self performSegueWithIdentifier:@"signOutSegue" sender:self];
}
-(void)syncForUpdates {
    NSLog(@"Timer working");
    __block int runsCount = 0;
    PFQuery *query = [PFQuery queryWithClassName:@"Runs"];
    [query whereKey:@"userID" equalTo:userId];
    [query countObjectsInBackgroundWithBlock:^(int count, NSError *error) {
        if (!error) {
            NSLog(@"User has gone on %i runs", count);
            runsCount = count;
        } else {
        }
    }];
    if (runsCount != [runArray count]) {
        [self refresh:nil];
    }
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return [runArray count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"runCell"];
        
    if (cell != nil) {
        runDictionary = [runArray objectAtIndex:indexPath.row];
        cell.textLabel.text = [runDictionary objectForKey:@"date"];
        //NSString *numToString = [
        cell.detailTextLabel.text = [[runDictionary objectForKey:@"distance"]stringValue];
        return cell;
    }
    return nil;
}
-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    
    [_distanceField resignFirstResponder];
    return YES;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
