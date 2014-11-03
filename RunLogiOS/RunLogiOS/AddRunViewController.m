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
    
    PFQuery *query = [PFQuery queryWithClassName:@"Runs"];
    //[query whereKey:@"playerName" equalTo:@"Dan Stemkoski"];
    runDictionary = [[NSMutableDictionary alloc]init];
    
    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (!error) {
            
            NSLog(@"Successfully retrieved %lu scores.", (unsigned long)objects.count);
            NSString *distancePulled = @"";
            NSString *datePulled = @"";
            
            for (PFObject *object in objects) {
                NSLog(@"%@", object.objectId);
                
                distancePulled = [object objectForKey:@"distance"];
                datePulled = [object objectForKey:@"date"];
                NSLog(@"Distance: %@ , Date: %@", distancePulled, datePulled);
                
                [runDictionary setValue:distancePulled forKey:@"distance"];
                [runDictionary setValue:datePulled forKey:@"date"];
                [runDictionary setValue:object.objectId forKey:@"objectID"];
                
                [runArray addObject:runDictionary];
                
            }
            [_runTable reloadData];
        } else {
            // Log details of the failure
            NSLog(@"Error: %@ %@", error, [error userInfo]);
        }
    }];
}
-(void)addRun:(id)sender {
    
    NSDate *date = [_runDate date];
    NSDateFormatter *df = [[NSDateFormatter alloc]init];
    [df setDateFormat:@"MM/dd/yyyy"];
    NSString *dateString = [df stringFromDate:date];
    NSString *distance = [_distanceField text];
    float distanceInt = [distance floatValue];
    
    PFObject *newRun = [PFObject objectWithClassName:@"Runs"];
    newRun[@"distance"] = distance;
    newRun[@"date"] = dateString;
    newRun[@"userID"] = @NO;
    [newRun saveInBackground];
    
    [runArray addObject:newRun];
    [runDictionary setValue:distance forKey:@"distance"];
    [runDictionary setValue:dateString forKey:@"date"];
    [_runTable reloadData];
}
-(void)deleteRun:(id)sender {
    
}
-(void)signOut:(id)sender {
    
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return [runArray count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"runCell"];
    for (int i = 0; i <[runArray count]; i++) {
        
   
        runDictionary = [runArray objectAtIndex:i];
        cell.textLabel.text = [runDictionary objectForKey:@"date"];
        cell.detailTextLabel.text = [runDictionary objectForKey:@"distance"];
    }
    
    return cell;
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
