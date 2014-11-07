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

@end

@implementation AddRunViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    runArray = [[NSMutableArray alloc]init];
    _distanceField.keyboardType = UIKeyboardTypeNumbersAndPunctuation;
    [_distanceField setDelegate:self];
    
    PFUser *currentUser = [PFUser currentUser];
    if (currentUser) {
        // do stuff with the user
        userId = currentUser.objectId;
        //[currentUser objectForKey:@"username"];
       // [_usernameLabel setText:[currentUser objectForKey:@"username"]];
        PFQuery *query = [PFQuery queryWithClassName:@"Runs"];
        [query whereKey:@"userID" equalTo:userId];
        runDictionary = [[NSMutableDictionary alloc]init];
        
        [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
            
            if (!error) {
                
                //NSString *distancePulled = @"";
                NSString *datePulled = @"";
                NSNumber *distancePulled = 0;
                
                for (PFObject *object in objects) {
                    NSLog(@"%@", object.objectId);
                    distancePulled = [object objectForKey:@"distance"];
                    datePulled = [object objectForKey:@"date"];
                    NSLog(@"Distance: %@ , Date: %@", distancePulled, datePulled);
                    NSMutableDictionary *runDictionary2 = [[NSMutableDictionary alloc]init];
                    
                    [runDictionary2 setValue:distancePulled forKey:@"distance"];
                    [runDictionary2 setValue:datePulled forKey:@"date"];
                    [runDictionary2 setValue:object.objectId forKey:@"objectId"];
                    
                    [runArray addObject:runDictionary2];
                    
                }
                [_runTable reloadData];
            } else {
                // Log details of the failure
                NSLog(@"Error: %@ %@", error, [error userInfo]);
            }
        }];

        
    } else {
        // show the signup or login screen
    }
    
}
-(void)addRun:(id)sender {
    
    NSDate *date = [_runDate date];
    NSDateFormatter *df = [[NSDateFormatter alloc]init];
    [df setDateFormat:@"MM/dd/yyyy"];
    NSString *dateString = [df stringFromDate:date];
    NSString *distance = [_distanceField text];
    NSNumberFormatter *numFormatter = [[NSNumberFormatter alloc] init];
    [numFormatter setNumberStyle:NSNumberFormatterDecimalStyle];
    NSNumber *distanceNum = [numFormatter numberFromString:distance];
    
    PFObject *newRun = [PFObject objectWithClassName:@"Runs"];
    newRun[@"distance"] = distanceNum;
    newRun[@"date"] = dateString;
    newRun[@"userID"] = userId;
    [newRun saveInBackground];
    
    [runArray addObject:newRun];
    NSMutableDictionary *runDictionary2 = [[NSMutableDictionary alloc]init];
    [runDictionary2 setValue:distanceNum forKey:@"distance"];
    [runDictionary2 setValue:dateString forKey:@"date"];
    [runArray removeAllObjects];
    [_distanceField setText:@""];
    PFQuery *query2 = [PFQuery queryWithClassName:@"Runs"];
    [query2 whereKey:@"userID" equalTo:userId];
    
    [query2 findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        
        if (!error) {
            
            NSString *datePulled = @"";
            NSNumber *distancePulled = 0;
            
            for (PFObject *object in objects) {
                NSLog(@"%@", object.objectId);
                distancePulled = [object objectForKey:@"distance"];
                datePulled = [object objectForKey:@"date"];
                NSLog(@"Distance: %@ , Date: %@", distancePulled, datePulled);
                NSMutableDictionary *runDictionary2 = [[NSMutableDictionary alloc]init];
                
                [runDictionary2 setValue:distancePulled forKey:@"distance"];
                [runDictionary2 setValue:datePulled forKey:@"date"];
                [runDictionary2 setValue:object.objectId forKey:@"objectId"];
                
                [runArray addObject:runDictionary2];
                
            }
            [_runTable reloadData];
        }
    }];
    [_runTable reloadData];


}
-(void)deleteRun:(id)sender {
    
    
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
                // Log details of the failure
                NSLog(@"Error: %@ %@", error, [error userInfo]);
            }
        }];
        [runArray removeObjectAtIndex:selectedIndexPath.row];
        [_runTable reloadData];
    }

}
-(void)signOut:(id)sender {
    [PFUser logOut];
    [self performSegueWithIdentifier:@"signOutSegue" sender:self];
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
   // }
    
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
